package com.example.interview;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
final class GithubProxyService {
    private final GithubProxyClient githubClient;

    GithubProxyService(GithubProxyClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<ResponseRepo> getRepositoriesForUser(String username) {
        GitHubRepo[] repos = githubClient.fetchRepositories(username);
        List<ResponseRepo> result = new ArrayList<>();

        for (GitHubRepo repo : repos) {
            if (repo.fork()) {
                continue;
            }

            List<ResponseBranch> branches = mapBranches(username, repo.name());
            result.add(new ResponseRepo(repo.name(), repo.owner().login(), branches));
        }

        return result;
    }

    private List<ResponseBranch> mapBranches(String username, String repoName) {
        GitHubBranch[] branches = githubClient.fetchBranches(username, repoName);
        List<ResponseBranch> result = new ArrayList<>(branches.length);

        for (GitHubBranch branch : branches) {
            result.add(new ResponseBranch(branch.name(), branch.commit().sha()));
        }

        return result;
    }
}
