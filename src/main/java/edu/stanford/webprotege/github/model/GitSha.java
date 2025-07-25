package edu.stanford.webprotege.github.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents a Git SHA (commit hash) value.
 *
 * <p>This is a value object used to encapsulate and validate commit SHA strings.</p>
 *
 * @param value the raw SHA string (must be non-null and non-blank)
 */
public record GitSha(@JsonValue String value) {

    /**
     * Constructs a new {@code GitSha} with validation.
     *
     * @param value the raw SHA string
     * @throws IllegalArgumentException if {@code value} is null or blank
     */
    @JsonCreator
    public GitSha {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("GitSha cannot be null or blank" );
        }
    }
}