package edu.stanford.webprotege.github.service;

import edu.stanford.webprotege.github.auth.GitHubJwtFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class GitHubAppInstallationCheckerService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubAppInstallationCheckerService.class);

    private final WebClient webClient;

    private final GitHubJwtFactory jwtFactory;

    public GitHubAppInstallationCheckerService(WebClient webClient, GitHubJwtFactory jwtFactory) {
        this.webClient = webClient;
        this.jwtFactory = jwtFactory;
    }

    public Mono<Boolean> isAppInstalledOnRepo(String owner, String repo) {
        var jwt = jwtFactory.getJwt();
        return webClient.get()
                .uri("/repos/{owner}/{repo}/installation", owner, repo)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.token())
                .retrieve()
                .toBodilessEntity()
                // Return true if the response is good (200) that indicates the app is installed
                .map(response -> true)
                // Return falls on a NotFound response that indicates the app is not installed
                .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.just(false)) // App not installed
                .onErrorResume(e -> {
                    logger.error("Error checking app installation for {}/{}: {}", owner, repo, e.getMessage(), e);
                    return Mono.error(e);
                });
    }

}
