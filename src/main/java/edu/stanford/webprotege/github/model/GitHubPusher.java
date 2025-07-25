package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The user who pushed the commit.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubPusher(
        /**
         * Git username of the pusher.
         */
        @JsonProperty("name" ) String name,

        /**
         * Email address of the pusher.
         */
        @JsonProperty("email" ) String email
) {

}
