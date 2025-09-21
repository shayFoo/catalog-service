package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.book.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenPostRequestThenBookCreated() {
        Book expected = new Book("1234567890", "Title", "Author", 9.90);
        webTestClient.post()
                .uri("/books")
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
}
