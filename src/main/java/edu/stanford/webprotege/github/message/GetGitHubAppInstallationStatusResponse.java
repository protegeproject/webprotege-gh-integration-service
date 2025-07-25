package edu.stanford.webprotege.github.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.webprotege.github.model.GitHubRepositoryCoordinates;
import edu.stanford.webprotege.github.model.GitHubAppInstallationStatus;

import java.util.Objects;

@JsonTypeName(GetGitHubAppInstallationStatusRequest.CHANNEL)
public record GetGitHubAppInstallationStatusResponse(@JsonProperty("projectId") ProjectId projectId,
                                                     @JsonProperty("repositoryCoordinates") GitHubRepositoryCoordinates repositoryCoordinates,
                                                     @JsonProperty("installationStatus") GitHubAppInstallationStatus installationStatus) implements Response {

    public GetGitHubAppInstallationStatusResponse {
        Objects.requireNonNull(projectId);
        Objects.requireNonNull(repositoryCoordinates);
        Objects.requireNonNull(installationStatus);
    }
}
