package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Represents an issue in a GitHub repository.
 *
 * @param id         the internal GitHub issue ID
 * @param number     the issue number (as shown in the GitHub UI)
 * @param title      the title of the issue
 * @param body       the full body text or description of the issue
 * @param state      the current state of the issue
 * @param user       the user who created the issue
 * @param labels     the list of labels applied to the issue
 * @param createdAt  the timestamp when the issue was created
 * @param updatedAt  the timestamp when the issue was last updated
 * @param closedAt   the timestamp when the issue was closed (may be {@code null})
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubIssue(
        @JsonProperty("id") long id,
        @JsonProperty("number") int number,
        @JsonProperty("title") String title,
        @JsonProperty("body") String body,
        @JsonProperty("state") GitHubIssueState state,
        @JsonProperty("user") GitHubUser user,
        @JsonProperty("labels") List<GitHubLabel> labels,
        @JsonProperty("created_at") OffsetDateTime createdAt,
        @JsonProperty("updated_at") OffsetDateTime updatedAt,
        @Nullable @JsonProperty("closed_at") OffsetDateTime closedAt
) {}