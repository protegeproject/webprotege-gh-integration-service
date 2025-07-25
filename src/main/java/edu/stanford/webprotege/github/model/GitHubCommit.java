package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A single commit in a {@code push} event.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubCommit(
        /**
         * The SHA of the commit.
         */
        @JsonProperty("id" ) String id,

        /**
         * The commit message.
         */
        @JsonProperty("message" ) String message,

        /**
         * The timestamp of the commit (ISO 8601).
         */
        @JsonProperty("timestamp" ) String timestamp,

        /**
         * Author information for the commit.
         */
        @JsonProperty("author" ) GitHubAuthor author,

        /**
         * The URL to view the commit on GitHub.
         */
        @JsonProperty("url" ) String url,

        /**
         * Files added in the commit.
         */
        @JsonProperty("added" ) List<String> added,

        /**
         * Files removed in the commit.
         */
        @JsonProperty("removed" ) List<String> removed,

        /**
         * Files modified in the commit.
         */
        @JsonProperty("modified" ) List<String> modified
) {

}
