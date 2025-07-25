package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author of a commit.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubAuthor(
        /**
         * Name of the author.
         */
        @JsonProperty("name" ) String name,

        /**
         * Email address of the author.
         */
        @JsonProperty("email" ) String email
) {

}
