package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Payload for a {@code push} event sent by GitHub webhooks.
 *
 * @param ref         the full Git ref that was pushed (e.g., {@code refs/heads/main})
 * @param before      the SHA of the commit before the push
 * @param after       the SHA of the commit after the push
 * @param repository  the repository to which the push was made
 * @param pusher      the user who performed the push operation
 * @param sender      the GitHub user who triggered the event
 * @param commits     the list of commits included in the push
 * @param head_commit the head commit of the push
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubPushEvent(
        @JsonProperty("ref") GitRef ref,
        @JsonProperty("before") GitSha before,
        @JsonProperty("after") GitSha after,
        @JsonProperty("repository") GitHubRepository repository,
        @JsonProperty("pusher") GitHubPusher pusher,
        @JsonProperty("sender") GitHubSender sender,
        @JsonProperty("commits") List<GitHubCommit> commits,
        @JsonProperty("head_commit") GitHubCommit head_commit
) {
    public static final String TYPE = "push";
}
