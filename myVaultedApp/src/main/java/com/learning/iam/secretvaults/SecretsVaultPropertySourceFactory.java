package com.learning.iam.secretvaults;

import org.jspecify.annotations.Nullable;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SecretsVaultPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
        // Example: Connect to Vault via HTTP API
        Map<String, Object> secrets = new HashMap<>();

        // In real code, use RestTemplate/WebClient to call Vault HTTP API
        // Example: GET http://127.0.0.1:8200/v1/secret/data/myapp
        SecretVaultClient secretVaultClient = new SecretVaultClient();
        secrets = secretVaultClient.getKeyValueSecrets("/mySecretVault/data/myApp");
        System.out.println(secrets);

        return new MapPropertySource(name, secrets);

    }
}
