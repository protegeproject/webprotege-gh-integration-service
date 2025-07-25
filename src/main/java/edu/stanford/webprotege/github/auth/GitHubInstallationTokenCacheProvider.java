package edu.stanford.webprotege.github.auth;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;
import edu.stanford.webprotege.github.model.GitHubInstallationId;

public class GitHubInstallationTokenCacheProvider {

    public Cache<GitHubInstallationId, GitHubInstallationToken> createTokenCache() {
        return Caffeine.newBuilder()
                .expireAfter(new GitHubInstallationTokenExpiry())
                .build();
    }
}
