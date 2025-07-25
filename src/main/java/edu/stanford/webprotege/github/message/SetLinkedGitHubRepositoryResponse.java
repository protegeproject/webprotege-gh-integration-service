package edu.stanford.webprotege.github.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.webprotege.github.model.GitHubRepositoryCoordinates;

import javax.annotation.Nonnull;
import java.util.Objects;

@JsonTypeName(SetLinkedGitHubRepositoryRequest.CHANNEL)
public record SetLinkedGitHubRepositoryResponse(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                @JsonProperty("repositoryCoordinates") GitHubRepositoryCoordinates repositoryCoordinates) implements Response {

    public SetLinkedGitHubRepositoryResponse(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                             @JsonProperty("repositoryCoordinates") GitHubRepositoryCoordinates repositoryCoordinates) {
        this.projectId = Objects.requireNonNull(projectId);
        this.repositoryCoordinates = Objects.requireNonNull(repositoryCoordinates);
    }
}
