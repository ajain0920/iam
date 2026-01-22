package com.learning.iam.secretvaults;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class SecretVaultClient {
    private static final JsonMapper jsonMapper = JsonMapper.builder().build();

    private final HttpClient httpClient;

    public SecretVaultClient() {
        this.httpClient = HttpClient.newBuilder().
                followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public Map<String, Object> getKeyValueSecrets(String kvPath) {

        //Steps should be first login and fetch token
        //eg setup role and secret for vault login and call login url with that details
        // it will generate and return a token in reponse which will be used to query secrets from vault
        // login url like vault_address/auth/approle/login
        // as we are using hashicorp vault in dev mode, token is defaulted to root
        //will call secret vault directly bypassing login using this token
        String url = "http://localhost:8200/v1" + kvPath;
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("x-vault-token", "root")
                .GET()
                .build();

        var response = sendRequest(request, 200);
        String responsebody = response.body();
        VaultKeyValueSecretResponse vaultSecretResponse = jsonMapper.readValue(responsebody, VaultKeyValueSecretResponse.class);
        return vaultSecretResponse.data().data();
    }

    private HttpResponse<String> sendRequest(HttpRequest request, int expectedStatusCode) {
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == expectedStatusCode) {
                return response;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return  response;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record  VaultKeyValueSecretResponse(VaultKeyValueData data) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record  VaultKeyValueData(Map<String, Object> data) {}
    }
}
