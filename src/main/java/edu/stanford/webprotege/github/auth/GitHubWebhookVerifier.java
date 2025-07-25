package edu.stanford.webprotege.github.auth;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Component for verifying GitHub webhook signatures using HMAC-SHA256.
 *
 * <p>GitHub signs each webhook request using the secret configured in the webhook settings.
 * This class provides a method to verify that the signature included in the
 * {@code X-Hub-Signature-256} header matches the expected value based on the request body and shared secret.
 */
public class GitHubWebhookVerifier {

    private static final Logger logger = LoggerFactory.getLogger(GitHubWebhookVerifier.class);

    private final String secret;

    /**
     * Creates a new verifier with the specified shared secret.
     *
     * @param secret the GitHub webhook secret used to compute HMAC-SHA256 signatures
     */
    public GitHubWebhookVerifier(String secret) {
        this.secret = secret;
    }

    /**
     * Verifies a GitHub webhook signature using HMAC-SHA256.
     *
     * @param payload             the raw request body received from GitHub
     * @param signature256Header the value of the {@code X-Hub-Signature-256} header (e.g., {@code sha256=...})
     * @return {@code true} if the signature is valid and matches the computed HMAC; {@code false} otherwise
     */
    public boolean verify(String payload, String signature256Header) {
        if(secret.isEmpty()) {
            logger.warn("No GitHub secret (${webprotege.github.webhook.secret}) has been specified. Skipping verification.");
            return true;
        }
        try {
            var expectedSignature = "sha256=" + hmacSha256(secret, payload);
            return slowEquals(expectedSignature, signature256Header);
        } catch (Exception e) {
            return false;
        }
    }

    private String hmacSha256(String secret, String data) throws Exception {
        var mac = Mac.getInstance("HmacSHA256");
        var keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);
        var hmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hmac);
    }

    /**
     * Compares two strings using a time-safe equality check to prevent timing attacks.
     *
     * @param a the first string
     * @param b the second string
     * @return {@code true} if the strings are equal; {@code false} otherwise
     */
    private boolean slowEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        var result = 0;
        for (var i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}