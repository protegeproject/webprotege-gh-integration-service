package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A GitHub user.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubUser(
        /**
         * GitHub login name.
         */
        @JsonProperty("login" ) String login,

        /**
         * GitHub user ID.
         */
        @JsonProperty("id" ) long id,

        /**
         * URL to the user’s avatar image.
         */
        @JsonProperty("avatar_url" ) String avatar_url,

        /**
         * URL to the user's GitHub profile.
         */
        @JsonProperty("html_url" ) String html_url
) {

}
