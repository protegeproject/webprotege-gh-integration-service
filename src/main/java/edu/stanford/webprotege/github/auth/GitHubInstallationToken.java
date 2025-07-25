package edu.stanford.webprotege.github.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.webprotege.github.model.GitHubPermissions;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubInstallationToken(@JsonProperty("token") String token,
                                      @JsonProperty("expires_at") Instant expiresAt,
                                      @JsonProperty("permissions") GitHubPermissions permissions) {
}
