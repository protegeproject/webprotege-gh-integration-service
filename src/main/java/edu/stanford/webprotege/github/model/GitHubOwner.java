package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Owner of the repository.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubOwner(
        /**
         * GitHub login name of the owner.
         */
        @JsonProperty("login" ) String login,

        /**
         * GitHub user ID of the owner.
         */
        @JsonProperty("id" ) long id,

        /**
         * URL to the owner's GitHub profile.
         */
        @JsonProperty("html_url" ) String html_url
) {

}
