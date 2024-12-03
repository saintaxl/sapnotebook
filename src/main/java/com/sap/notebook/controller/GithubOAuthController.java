package com.sap.notebook.controller;

import com.sap.notebook.domain.ResponseWrapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/github/oauth")
public class GithubOAuthController {

    private static final Logger LOG = LoggerFactory.getLogger(GithubAppController.class);

    private static final String SUCCESS = "Success";

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public String callback(@RequestHeader(required = false) Map<String, String> headers,
                                                    @RequestParam(value = "code") String code,
                                                    @RequestParam(value = "installation_id") String installationId,
                                                    @RequestParam(value = "setup_action") String setupAction) {

        LOG.info("request recieved ...");

        LOG.info("Parametes: code:{}, installationId:{}, setupAction:{}", code, installationId, setupAction);

        LOG.info("response sent ");

        String githubAuthUrl = "https://github.com/login/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=repo,user,write:repo_hook&state=abcdefg";
        return "redirect:" + githubAuthUrl;
    }

    @RequestMapping(value = "/authorize_callback", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> authorizeCallback(@RequestHeader(required = false) Map<String, String> headers,
                                                             @RequestParam(required = false, value = "code") String code,
                                                             @RequestParam(required = false, value = "state") String state) {

        LOG.info("request recieved ...");

        LOG.info("Parametes: code:{}, state:{}", code, state);

        String accessToken = getAccessToken(code);

        LOG.info("response sent accessToken:{}", accessToken);
        return new ResponseEntity<>(new ResponseWrapper(SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/login")
    public String login() {
        String githubAuthUrl = "https://github.com/login/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=repo,user,write:repo_hook&state=abcdefg";
        return "redirect:" + githubAuthUrl;
    }

    private String getAccessToken(String code) {
        String tokenUrl = "https://github.com/login/oauth/access_token";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("code", code);
        params.put("redirect_uri", redirectUri);

        Map<String, String> response = restTemplate.postForObject(tokenUrl, params, Map.class);
        LOG.info("Response from AccessToken:");
        response.forEach((key, value) -> LOG.info("key:{},value:{}", key, value));
        return response.get("access_token");
    }

    @GetMapping("/authcode")
    public String authcode() {
        String url = "https://github.com/login/oauth/authorize?client_id=Ov23lit5drkHIse9M5XI&redirect_uri=https://tunnal.jadecloud.top:8080/github/oauth/callback&scope=repo,user,write:repo_hook&state=abcdefg";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);


            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                System.out.println("ddd");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
