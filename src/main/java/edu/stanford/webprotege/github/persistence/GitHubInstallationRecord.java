package edu.stanford.webprotege.github.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.webprotege.github.model.GitHubInstallationId;

public record GitHubInstallationRecord(@JsonProperty("projectId") ProjectId projectId,
                                       @JsonProperty("installationId") GitHubInstallationId installationId) {

}
