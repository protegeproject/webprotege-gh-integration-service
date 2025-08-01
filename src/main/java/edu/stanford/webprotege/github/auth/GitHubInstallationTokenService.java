package edu.stanford.webprotege.github.auth;

import com.github.benmanes.caffeine.cache.Cache;
import edu.stanford.webprotege.github.model.GitHubInstallationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GitHubInstallationTokenService {
    private static final Logger logger = LoggerFactory.getLogger(GitHubInstallationTokenService.class);

    private static final String INSTALLATION_TOKEN_URI = "/app/installations/{installation_id}/access_tokens";

    private final GitHubJwtFactory jwtFactory;

    private final WebClient webClient;

    private final Cache<GitHubInstallationId, GitHubInstallationToken> tokenCache;

    public GitHubInstallationTokenService(
            GitHubJwtFactory jwtFactory,
            WebClient webClient,
            Cache<GitHubInstallationId, GitHubInstallationToken> tokenCache) {
        this.jwtFactory = jwtFactory;
        this.webClient = webClient;
        this.tokenCache = tokenCache;
    }

    /**
     * Asynchronously gets a cached or freshly fetched installation token.  Fresh tokens will be
     * returned if the token is within some margin of the exiry time.
     */
    public Mono<GitHubInstallationToken> getInstallationToken(GitHubInstallationId installationId) {
        GitHubInstallationToken cached = tokenCache.getIfPresent(installationId);
        if (cached != null) {
            return Mono.just(cached);
        }

        return fetchNewToken(installationId)
                .doOnNext(token -> tokenCache.put(installationId, token));
    }

    private Mono<GitHubInstallationToken> fetchNewToken(GitHubInstallationId installationId) {
        var jwt = jwtFactory.getJwt();
        logger.info("Requesting installation token for installation ID {}", installationId);
        return webClient.post()
                .uri(INSTALLATION_TOKEN_URI, installationId.id())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.token())
                .retrieve()
                .bodyToMono(GitHubInstallationToken.class)
                .doOnError(error ->
                        logger.warn("Failed to fetch token for installation {}", installationId, error));
    }
}
