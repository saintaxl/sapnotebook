package com.sap.notebook.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.notebook.utils.JwtUtils;
import lombok.SneakyThrows;
import org.kohsuke.github.GHEvent;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHHook;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class GitHubService {

    private static final Logger LOG = LoggerFactory.getLogger(GitHubService.class);

    @Value("${github.app.id}")
    private String appId;

    @Value("${github.app.private-key}")
    private String privateKeyPath;

    @Value("${webhook.url}")
    private String webhookUrl;

    private Map<String, String> webhookSecrets = new HashMap<>();

    private final JwtUtils jwtUtils;

    public GitHubService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public String getInstallationId(String jwt) throws IOException {
        URL url = new URL("https://api.github.com/app/installations");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + jwt);
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.toString());
            return rootNode.get(0).get("id").asText();
        } else {
            throw new RuntimeException("Failed to get installation ID: " + responseCode);
        }
    }

    public String getAccessToken(String installationId, String jwt) throws IOException {
        URL url = new URL("https://api.github.com/app/installations/" + installationId + "/access_tokens");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + jwt);
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        int responseCode = connection.getResponseCode();
        if (responseCode == 201) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.toString());
            return rootNode.get("token").asText();
        } else {
            throw new RuntimeException("Failed to get access token: " + responseCode);
        }
    }

    public String   generateJWT() throws IOException {
        return jwtUtils.generateJWT(appId, privateKeyPath);
    }

    @SneakyThrows
    public GHEventPayload.Installation getGHInstallationPayload(String accessToken, String payload){
        GitHub github = new GitHubBuilder().withOAuthToken(accessToken).build();
        GHEventPayload.Installation installationPayload = github.parseEventPayload(new StringReader(payload), GHEventPayload.Installation.class);
        return installationPayload;
    }

    @SneakyThrows
    public GHEventPayload.InstallationRepositories getGHInstallationRepositoriesPayload(String accessToken, String payload){
        GitHub github = new GitHubBuilder().withOAuthToken(accessToken).build();
        GHEventPayload.InstallationRepositories installationRepositories = github.parseEventPayload(new StringReader(payload), GHEventPayload.InstallationRepositories.class);
        return installationRepositories;
    }

    public void createWebhookForRepo(String repoName, String accessToken) throws IOException {
        GitHub github = new GitHubBuilder().withOAuthToken(accessToken).build();
        GHRepository repository = github.getRepository(repoName);

        // Generate a unique secret for the webhook
        String webhookSecret = generateSecret();

        // Store the secret for later use
        webhookSecrets.put(repoName, webhookSecret);

        // Create the webhook
        Map<String, String> config = new HashMap<>();
        config.put("url", webhookUrl);
        config.put("content_type", "json");
        config.put("secret", webhookSecret);

        GHHook hookResp = repository.createHook("web", config, Collections.singletonList(GHEvent.PUSH), true);
        LOG.info("Create Webhook successful.");
    }

    private String generateSecret() {
        // Generate a random secret
        byte[] array = new byte[16];
        new Random().nextBytes(array);
        return Base64.getEncoder().encodeToString(array);
    }

    public String getWebhookSecret(String repoName) {
        return webhookSecrets.get(repoName);
    }


}
