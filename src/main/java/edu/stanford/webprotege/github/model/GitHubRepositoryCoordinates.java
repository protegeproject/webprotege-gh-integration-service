package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-21
 * <p>
 * Constructs a new GitHubRepositoryCoordinates object with the specified owner and repository names.
 */
public record GitHubRepositoryCoordinates(@JsonProperty("ownerName") @Nonnull String ownerName,
                                          @JsonProperty("repositoryName") @Nonnull String repositoryName) {

    public static final Pattern NAME_PATTERN = Pattern.compile("[A-Za-z0-9_.-]+");

    public static final Pattern FULL_NAME_PATTERN = Pattern.compile("^(" + NAME_PATTERN.pattern() + ")/(" + NAME_PATTERN.pattern() + ")$");

    public GitHubRepositoryCoordinates {
        Objects.requireNonNull(ownerName);
        Objects.requireNonNull(repositoryName);
    }

    @JsonCreator
    @Nonnull
    public static GitHubRepositoryCoordinates of(@JsonProperty("ownerName") @Nonnull String ownerName,
                                                 @JsonProperty("repositoryName") @Nonnull String repositoryName) {
        Objects.requireNonNull(ownerName);
        Objects.requireNonNull(repositoryName);
        return new GitHubRepositoryCoordinates(ownerName, repositoryName);
    }

    public static GitHubRepositoryCoordinates fromFullName(@Nonnull String fullName) {
        Objects.requireNonNull(fullName);
        var fullNameMatcher = FULL_NAME_PATTERN.matcher(fullName);
        if (!fullNameMatcher.matches()) {
            throw new IllegalArgumentException("Invalid full name");
        }
        return GitHubRepositoryCoordinates.of(fullNameMatcher.group(1), fullNameMatcher.group(2));
    }

    /**
     * Gets the full name of the GitHub repository in the format "ownerName/repositoryName."
     *
     * @return The full name of the GitHub repository.
     */
    @JsonIgnore
    public String getFullName() {
        return ownerName() + "/" + repositoryName();
    }
}