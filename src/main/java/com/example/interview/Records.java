package com.example.interview;

import java.util.List;

record ErrorResponse(int status, String message) {}

record GitHubRepo(String name, Owner owner, boolean fork) {}
record Owner(String login) {}

record GitHubBranch(String name, Commit commit) {}
record Commit(String sha) {}

record ResponseRepo(String repositoryName, String ownerLogin, List<ResponseBranch> branches) {}
record ResponseBranch(String name, String lastCommitSha) {}
