package com.schimidt.observability.users;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.crypto")
public record EncryptionProperties(int iterations, int keyLength, String algorithm) {
}
