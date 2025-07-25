package edu.stanford.webprotege.github.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;
import edu.stanford.webprotege.github.model.GitHubRepositoryCoordinates;

import static edu.stanford.webprotege.github.message.GetGitHubAppInstallationStatusRequest.CHANNEL;

@JsonTypeName(CHANNEL)
public record GetGitHubAppInstallationStatusRequest(@JsonProperty("projectId") ProjectId projectId,
                                                    @JsonProperty("repositoryCoordinates") GitHubRepositoryCoordinates repositoryCoordinates) implements ProjectRequest<GetGitHubAppInstallationStatusResponse> {

    public static final String CHANNEL = "webprotege.github.GetGitHubAppInstallationStatus";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
