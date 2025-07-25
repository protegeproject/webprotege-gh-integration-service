package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * A summary representation of a GitHub repository.
 *
 * @param id          the internal GitHub repository ID
 * @param nodeId      the GraphQL node ID of the repository
 * @param name        the short name of the repository (e.g., {@code webprotege})
 * @param fullName    the full name of the repository including owner (e.g., {@code stanford-webprotege/webprotege})
 * @param description the repository description
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubRepository(
        @JsonProperty("id" ) long id,
        @JsonProperty("node_id" ) String nodeId,
        @JsonProperty("name" ) String name,
        @JsonProperty("full_name" ) String fullName,
        @JsonProperty("description" ) String description
) {

    /**
     * Constructs a {@code GitHubRepository}, replacing null values with empty strings where applicable.
     */
    public GitHubRepository(@JsonProperty("id" ) long id,
                            @JsonProperty("node_id" ) String nodeId,
                            @JsonProperty("name" ) String name,
                            @JsonProperty("full_name" ) String fullName,
                            @JsonProperty("description" ) String description) {
        this.id = id;
        this.nodeId = Objects.requireNonNullElse(nodeId, "" );
        this.name = Objects.requireNonNullElse(name, "" );
        this.fullName = Objects.requireNonNullElse(fullName, "" );
        this.description = Objects.requireNonNullElse(description, "" );
    }

    /**
     * Factory method for Jackson deserialization with null-safe defaults.
     *
     * @param id          the internal GitHub repository ID
     * @param nodeId      the GraphQL node ID of the repository
     * @param name        the short name of the repository
     * @param fullName    the full name of the repository including owner
     * @param description the repository description
     * @return a new {@code GitHubRepository} instance
     */
    @JsonCreator
    public static GitHubRepository create(
            @JsonProperty("id" ) long id,
            @JsonProperty("node_id" ) String nodeId,
            @JsonProperty("name" ) String name,
            @JsonProperty("full_name" ) String fullName,
            @JsonProperty("description" ) String description
    ) {
        return new GitHubRepository(id, nodeId, name, fullName, description);
    }

    /**
     * Returns a parsed coordinate object (owner/repo) from the {@code full_name}.
     *
     * @return GitHub repository coordinates
     */
    @JsonIgnore
    public GitHubRepositoryCoordinates getCoordinates() {
        return GitHubRepositoryCoordinates.fromFullName(fullName);
    }
}
