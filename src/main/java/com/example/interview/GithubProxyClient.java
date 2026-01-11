package com.example.interview;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
final class GithubProxyClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    GithubProxyClient(RestTemplate restTemplate,
                      @Value("${github.base-url:https://api.github.com}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public GitHubRepo[] fetchRepositories(String username) {
        return restTemplate.getForObject(baseUrl + "/users/" + username + "/repos", GitHubRepo[].class);
    }

    public GitHubBranch[] fetchBranches(String username, String repoName) {
        return restTemplate.getForObject(baseUrl + "/repos/" + username + "/" + repoName + "/branches", GitHubBranch[].class);
    }
}
