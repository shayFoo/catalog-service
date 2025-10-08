package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.book.Book;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration")
@Testcontainers
class CatalogServiceApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Container
    private static final KeycloakContainer keycloakContainer =
            new KeycloakContainer("keycloak/keycloak:26.4")
                    .withRealmImportFile("test-realm-config.json");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "/realms/PolarBookshop");
    }

    private static AccessTokenResponse bjornToken;
    private static AccessTokenResponse isabelleToken;

    @BeforeAll
    static void generateAccessToken() {
        String baseUrl = keycloakContainer.getAuthServerUrl()
                + "/realms/PolarBookshop/protocol/openid-connect/token";
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
        bjornToken = authenticateWith("bjorn", webClient);
        isabelleToken = authenticateWith("isabelle", webClient);
    }

    private static AccessTokenResponse authenticateWith(String username, WebClient webClient) {
        return webClient
                .post()
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "polar-test")
                        .with("username", username)
                        .with("password", "password")
                )
                .retrieve()
                .bodyToMono(AccessTokenResponse.class)
                .block();
    }

    @Test
    void whenPostRequestThenBookCreated() {
        Book expected = new Book("1234567890", "Title", "Author", 9.90, "publisher");
        webTestClient.post()
                .uri("/books")
                .headers(headers -> headers.setBearerAuth(isabelleToken.getToken()))
                .bodyValue(expected)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Book.class)
                .value(book -> {
                    assertThat(book).isNotNull();
                    assertThat(book.isbn()).isEqualTo(expected.isbn());
                    assertThat(book.title()).isEqualTo(expected.title());
                    assertThat(book.author()).isEqualTo(expected.author());
                    assertThat(book.price()).isEqualTo(expected.price());
                });
    }

    @Test
    void whenPostRequestUnauthorizedThen403() {
        Book expected = new Book("1234567890", "Title", "Author", 9.90, "publisher");
        webTestClient.post()
                .uri("/books")
                .headers(headers -> headers.setBearerAuth(bjornToken.getToken()))
                .bodyValue(expected)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenPostRequestUnauthenticatedThen401() {
        Book expected = new Book("1234567890", "Title", "Author", 9.90, "publisher");
        webTestClient.post()
                .uri("/books")
                .bodyValue(expected)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
