package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the state of a GitHub issue.
 */
public enum GitHubIssueState {

    /** The issue is open and accepting comments or changes. */
    @JsonProperty("open")
    OPEN,

    /** The issue is closed and no longer active. */
    @JsonProperty("closed")
    CLOSED;

    /**
     * Creates a {@code GitHubIssueState} from a lowercase string value.
     *
     * @param value the string from the GitHub API (e.g., "open", "closed")
     * @return the corresponding enum value
     * @throws IllegalArgumentException if the value is invalid
     */
    @JsonCreator
    public static GitHubIssueState fromString(String value) {
        for (GitHubIssueState state : values()) {
            JsonProperty annotation;
            try {
                annotation = GitHubIssueState.class
                        .getField(state.name())
                        .getAnnotation(JsonProperty.class);
                if (annotation != null && annotation.value().equals(value)) {
                    return state;
                }
            } catch (NoSuchFieldException ignored) {}
        }
        throw new IllegalArgumentException("Invalid GitHubIssueState: " + value);
    }
}