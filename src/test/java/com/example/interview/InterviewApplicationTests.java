package com.example.interview;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.*;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
final class InterviewApplicationTests {

	static WireMockServer wireMock;

	@Autowired
	TestRestTemplate restTemplate;

	@BeforeAll
	static void setup() {
		wireMock = new WireMockServer(wireMockConfig().dynamicPort());
		wireMock.start();
		configureFor("localhost", wireMock.port());
	}

	@AfterAll
	static void teardown() {
		wireMock.stop();
	}

	@DynamicPropertySource
	static void githubProps(DynamicPropertyRegistry r) {
		r.add("github.base-url", () -> "http://localhost:" + wireMock.port());
	}

	@Test
	void returnsNonForkReposWithBranches() {
		stubFor(get("/users/john/repos").willReturn(okJson("""
            [
              {"name":"repo","fork":false,"owner":{"login":"john"}},
              {"name":"forked","fork":true,"owner":{"login":"john"}}
            ]
        """)));

		stubFor(get("/repos/john/repo/branches").willReturn(okJson("""
            [
              {"name":"main","commit":{"sha":"abc"}},
              {"name":"dev","commit":{"sha":"def"}}
            ]
        """)));

		ResponseEntity<ResponseRepo[]> response =
				restTemplate.getForEntity("/api/repositories/john", ResponseRepo[].class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).hasSize(1);

		var repo = response.getBody()[0];
		assertThat(repo.repositoryName()).isEqualTo("repo");
		assertThat(repo.branches()).hasSize(2);
	}

	@Test
	void returns404WhenUserNotFound() {
		stubFor(get("/users/missing/repos").willReturn(aResponse().withStatus(404)));

		ResponseEntity<ErrorResponse> response =
				restTemplate.getForEntity("/api/repositories/missing", ErrorResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(Objects.requireNonNull(response.getBody()).status()).isEqualTo(404);
	}
}
