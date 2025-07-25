package edu.stanford.webprotege.github.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.webprotege.github.auth.GitHubWebhookVerifier;
import edu.stanford.webprotege.github.message.WebProtegeGitHubIssuesEvent;
import edu.stanford.webprotege.github.message.WebProtegeGitHubPushEvent;
import edu.stanford.webprotege.github.model.GitHubIssuesEvent;
import edu.stanford.webprotege.github.model.GitHubPushEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(GitHubWebhookController.class);

    private final ObjectMapper objectMapper;

    private final GitHubWebhookVerifier webhookVerifier;

    private final EventDispatcher eventDispatcher;

    public GitHubWebhookController(ObjectMapper objectMapper, GitHubWebhookVerifier webhookVerifier, EventDispatcher eventDispatcher) {
        this.objectMapper = objectMapper;
        this.webhookVerifier = webhookVerifier;
        this.eventDispatcher = eventDispatcher;
    }

    @PostMapping("/github/webhooks")
    public ResponseEntity<?> handleGitHubWebhook(@RequestHeader("X-GitHub-Event") String event,
                                                 @RequestHeader("X-Hub-Signature-256") String signature256,
                                                 @RequestBody String payload) {
        var verified = webhookVerifier.verify(payload, signature256);
        if(!verified) {
            logger.warn("GitHub webhook verification failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        logger.info("Handling webhook event: {}", event);
        try {
            switch (event) {
                case GitHubPushEvent.TYPE -> {
                    var pushEvent = objectMapper.readValue(payload, GitHubPushEvent.class);
                    var webprotegeEvent = new WebProtegeGitHubPushEvent(EventId.generate(), pushEvent);
                    eventDispatcher.dispatchEvent(webprotegeEvent);
                }
                case GitHubIssuesEvent.TYPE -> {
                    var issuesEvent = objectMapper.readValue(payload, GitHubIssuesEvent.class);
                    var webprotegeEvent = new WebProtegeGitHubIssuesEvent(EventId.generate(), issuesEvent);
                    eventDispatcher.dispatchEvent(webprotegeEvent);

                }
            }
            return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            logger.error("Error parsing GitHub webhook payload", e);
            return ResponseEntity.internalServerError().body("Error parsing GitHub webhook payload");
        }
    }

}
