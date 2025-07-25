package edu.stanford.webprotege.github.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.webprotege.github.model.GitHubRepositoryCoordinates;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-20
 */
@Document(collection = "LinkedGitHubRepositories")
public record LinkedGitHubRepositoryRecord(@Id @Indexed @JsonProperty("projectId") String projectId,
                                           @JsonProperty("repositoryCoordinates") GitHubRepositoryCoordinates repositoryCoordinates) {

    public static LinkedGitHubRepositoryRecord of(ProjectId projectId, GitHubRepositoryCoordinates repositoryCoordinates) {
        return new LinkedGitHubRepositoryRecord(projectId.id(), repositoryCoordinates);
    }
}
