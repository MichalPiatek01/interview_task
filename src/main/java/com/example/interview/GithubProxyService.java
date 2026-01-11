package com.example.interview;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
final class GithubProxyService {
    private final GithubProxyClient githubClient;

    GithubProxyService(GithubProxyClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<ResponseRepo> getRepositoriesForUser(String username) {
        GitHubRepo[] repos = githubClient.fetchRepositories(username);

        return Arrays.stream(repos)
                .filter(repo -> !repo.fork())
                .map(repo -> {
                    List<GitHubBranch> branches = Arrays.asList(githubClient.fetchBranches(username, repo.name()));
                    List<ResponseBranch> responseBranches = branches.stream()
                            .map(b -> new ResponseBranch(b.name(), b.commit().sha()))
                            .toList();
                    return new ResponseRepo(repo.name(), repo.owner().login(), responseBranches);
                })
                .toList();
    }
}
