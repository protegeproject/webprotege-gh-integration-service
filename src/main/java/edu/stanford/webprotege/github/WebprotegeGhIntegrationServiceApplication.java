package edu.stanford.webprotege.github;

import com.github.benmanes.caffeine.cache.Cache;
import edu.stanford.protege.webprotege.common.WebProtegeCommonConfiguration;
import edu.stanford.protege.webprotege.ipc.WebProtegeIpcApplication;
import edu.stanford.protege.webprotege.ipc.impl.RabbitMqProperties;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import edu.stanford.webprotege.github.auth.GitHubInstallationToken;
import edu.stanford.webprotege.github.auth.GitHubInstallationTokenCacheProvider;
import edu.stanford.webprotege.github.auth.GitHubWebhookVerifier;
import edu.stanford.webprotege.github.model.GitHubInstallationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@Import({WebProtegeCommonConfiguration.class,
        WebProtegeIpcApplication.class,
        WebProtegeJacksonApplication.class})
@EnableConfigurationProperties
public class WebprotegeGhIntegrationServiceApplication implements ApplicationRunner {

    private final static Logger logger = LoggerFactory.getLogger(WebprotegeGhIntegrationServiceApplication.class);

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(WebprotegeGhIntegrationServiceApplication.class, args);
    }

    @Bean
    public WebClient githubWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.github.com" )
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json" )
                .build();
    }

    @Bean
    GitHubInstallationTokenCacheProvider gitHubInstallationTokenCacheProvider() {
        return new GitHubInstallationTokenCacheProvider();
    }

    @Bean
    public Cache<GitHubInstallationId, GitHubInstallationToken> tokenCache(GitHubInstallationTokenCacheProvider tokenCacheProvider) {
        return tokenCacheProvider.createTokenCache();
    }

    @Bean
    GitHubWebhookVerifier gitHubWebhookVerifier(@Value("${webprotege.github.webhook.secret:}") String secret) {
        if(secret.isEmpty()) {
            logger.warn("Missing required property: webprotege.github.webhook.secret");
        }
        return new GitHubWebhookVerifier(secret);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
