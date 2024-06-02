package com.sap.notebook.controller;

import com.sap.notebook.domain.ResponseWrapper;
import com.sap.notebook.services.GitHubService;
import lombok.SneakyThrows;
import org.kohsuke.github.GHEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/github/app")
public class GithubAppController {

    private static final Logger LOG = LoggerFactory.getLogger(GithubAppController.class);

    private static final String SIGNATURE = "x-hub-signature";
    private static final String SUCCESS = "Success";
    private static final String ERROR = "Error";

    @Value("${github.webhook.secret}")
    private String webhookSecret;

    @Value("${github.app.id}")
    private String appId;

    @Value("${github.app.private-key}")
    private String privateKeyPath;

    @Autowired
    private GitHubService gitHubService;

    @GetMapping("/helloworld")
    public String getHelloWorld() {
        return "Hello World webhook";
    }

    @SneakyThrows
    @RequestMapping(value = "/webhook", method = { RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<ResponseWrapper> webhooks(@RequestHeader(required = false) Map<String, String> headers,
                                                    @RequestHeader(required = false,value="X-GitHub-Event") String eventType,
                                                    @RequestHeader(required = false, value="X-Hub-Signature-256") String signature,
                                                    @RequestBody(required = false) String payload) {


        // if signature is empty return 401
        if (!StringUtils.hasText(signature)) {
            return new ResponseEntity<>(new ResponseWrapper(ERROR), HttpStatus.FORBIDDEN);
        }

        // if payload is empty, don't do anything
        if (!StringUtils.hasText(payload)) {
            new ResponseEntity<>(new ResponseWrapper(SUCCESS), HttpStatus.OK);
        }

        verifySignature(webhookSecret, payload);

        LOG.info("Received event:{}:", eventType);

        switch (eventType) {
            case "push":
                handlePushEvent(payload);
                break;
            case "installation":
                handleInstallationEvent(payload);
                break;
            case "installation_repositories":
                handleInstallationReposEvent(payload);
                break;
            case "github_app_authorization":
                break;
            case "ping":
                break;
            default:
                System.out.println("Unhandled event type: " + eventType);
        }

        LOG.info("request recieved ...");

        LOG.info("Payload: {}", payload);

        LOG.info("response sent ");
        return new ResponseEntity<>(new ResponseWrapper(SUCCESS), HttpStatus.OK);
    }

    @SneakyThrows
    private void handleInstallationReposEvent(String payload) {
        String jwt = gitHubService.generateJWT();
        String installationId = gitHubService.getInstallationId(jwt);
        String accessToken = gitHubService.getAccessToken(installationId, jwt);
        LOG.info("Access Token: " + accessToken);

        GHEventPayload.InstallationRepositories installation = gitHubService.getGHInstallationRepositoriesPayload(accessToken, payload);
        switch (installation.getAction()){
            case "added":
                gitHubService.createWebhookForRepo(installation.getRepositoriesAdded().get(0).getFullName(), accessToken);
                break;
            case "removed":
                break;
            default:
                break;
        }
    }

    @SneakyThrows
    private void handleInstallationEvent(String payload) {
        String jwt = gitHubService.generateJWT();
        String installationId = gitHubService.getInstallationId(jwt);
        String accessToken = gitHubService.getAccessToken(installationId, jwt);
        LOG.info("Access Token: " + accessToken);

        GHEventPayload.Installation installation = gitHubService.getGHInstallationPayload(accessToken, payload);
        switch (installation.getAction()){
            case "created":
                gitHubService.createWebhookForRepo(installation.getRepositories().get(0).getFullName(), accessToken);
                break;
            case "deleted":
                break;
            case "new_permissions_accepted":
                break;
            case "unsuspend":
                break;
            case "suspend":
                break;
            default:
                break;
        }

    }

    private void handlePushEvent(String payload) {
        System.out.println("Received push event: " + payload);
        System.out.println("Push event End.");
    }

    private boolean verifySignature(String signature, String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(webhookSecret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(payload.getBytes());
            String expectedSignature = "sha256=" + Base64.getEncoder().encodeToString(hash);
            return expectedSignature.equals(signature);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify signature", e);
        }
    }


}
