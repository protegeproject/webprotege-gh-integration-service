package edu.stanford.webprotege.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.github.server.GitHubRepositoryCoordinates;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-20
 */
@Document(collection = "LinkedGitHubRepositoryRecords")
public record LinkedGitHubRepositoryRecord(@Id @Indexed @JsonProperty("projectId") ProjectId projectId,
                                           @JsonProperty("repositoryCoordinates") GitHubRepositoryCoordinates repositoryCoordinates) {


}
