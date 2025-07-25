package edu.stanford.webprotege.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GitHubInstallationRepositoriesResponse(@JsonProperty("repositories") List<GitHubRepository> repositories) {

}
