package com.example.interview;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/repositories")
final class GithubProxyController {
    private final GithubProxyService githubService;

    GithubProxyController(GithubProxyService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{username}")
    public List<ResponseRepo> getUserRepositories(@PathVariable String username) {
        return githubService.getRepositoriesForUser(username);
    }
}
