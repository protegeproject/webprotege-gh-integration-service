package edu.stanford.webprotege.github.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.webprotege.github.persistence.GitHubInstallationRepository;
import edu.stanford.webprotege.github.model.GitHubInstallationId;
import edu.stanford.webprotege.github.model.GitHubState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
public class GitHubAppController {

    private static final Logger logger = LoggerFactory.getLogger(GitHubAppController.class);

    private final GitHubInstallationRepository repository;

    private final ObjectMapper objectMapper;

    public GitHubAppController(GitHubInstallationRepository repository,
                               ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @GetMapping(path = "/github/install/installed")
    public ResponseEntity<?> handleInstallAuthorized(@RequestParam("installation_id") GitHubInstallationId installationId,
                                                     @RequestParam("state") String stateParam) throws UnsupportedEncodingException {
        var state = getGitHubState(stateParam);
        logger.info("Linking GitHub app installation Id to project {}", state.projectId());
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/github/install/complete")
    public ResponseEntity<?> handleInstallComplete(@RequestParam("installation_id") GitHubInstallationId installationId,
                                                   @RequestParam("state") String stateParam) {
        var state = getGitHubState(stateParam);
        logger.info("Installation complete for project {} with installationId: {}", state.projectId(), installationId);
        repository.setInstallationId(state.projectId(), installationId);
        // Redirect to follow to
        return ResponseEntity.status(HttpStatus.FOUND).body(state.returnTo());
    }

    private GitHubState getGitHubState(String stateParam) {
        try {
            var decodedStateParam = URLDecoder.decode(stateParam, StandardCharsets.UTF_8);
            return objectMapper.readValue(decodedStateParam, GitHubState.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
