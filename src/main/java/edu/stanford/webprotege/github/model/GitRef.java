package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents a Git reference (e.g., {@code refs/heads/main}, {@code refs/tags/v1.0.0}).
 *
 * @param value the raw ref string (must be non-null and non-blank)
 */
public record GitRef(@JsonValue String value) {

    /**
     * Constructs a new {@code GitRef}, validating the input.
     *
     * @param value the full Git ref string
     * @throws IllegalArgumentException if the value is null or blank
     */
    @JsonCreator
    public GitRef {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("GitRef cannot be null or blank");
        }
    }
}
