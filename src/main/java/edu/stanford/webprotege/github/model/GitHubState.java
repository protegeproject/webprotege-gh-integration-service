package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.ProjectId;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubState(@JsonProperty("returnTo") String returnTo,
                          @JsonProperty("projectId") ProjectId projectId) {

    public GitHubState(@JsonProperty("returnTo" ) String returnTo, @JsonProperty("projectId" ) ProjectId projectId) {
        this.returnTo = Objects.requireNonNull(returnTo);
        this.projectId = Objects.requireNonNull(projectId);
    }
}
