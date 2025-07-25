package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonValue;

public record GitHubInstallationId(@JsonValue String id) {

    public static GitHubInstallationId valueOf(String id) {
        return new GitHubInstallationId(id);
    }
}
