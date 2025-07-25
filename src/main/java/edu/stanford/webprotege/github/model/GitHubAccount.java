package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubAccount(
        @JsonProperty("login" ) String login,
        @JsonProperty("id" ) long id,
        @JsonProperty("type" ) String type,
        @JsonProperty("html_url" ) String htmlUrl
) {

}
