package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A label attached to an issue.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubLabel(
        /**
         * Internal label ID.
         */
        @JsonProperty("id" ) long id,

        /**
         * Name of the label.
         */
        @JsonProperty("name" ) String name,

        /**
         * Color code of the label (hex, no #).
         */
        @JsonProperty("color" ) String color
) {

}
