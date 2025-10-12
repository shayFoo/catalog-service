package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.config.SecurityConfig;
import com.polarbookshop.catalogservice.domain.book.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.book.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@WebMvcTest(BookController.class)
@ActiveProfiles("integration")
@Import(SecurityConfig.class)
class BookControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    JwtDecoder jwtDecoder;

    @Test
    void whenGetBookNotExistsThenReturn404() {
        String isbn = "1234567890";
        BDDMockito.given(bookService.viewBookDetails(isbn))
                .willThrow(new BookNotFoundException(isbn));
        MvcTestResult result = MockMvcTester.create(mockMvc)
                .get()
                .uri("/books/{isbn}", isbn)
                .exchange();
        result.assertThat()
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenDeleteBookWithEmployeeRoleThenShouldReturn204() {
        String isbn = "7373731394";
        MockMvcTester mvcTester = MockMvcTester.create(mockMvc);
        mvcTester.delete()
                .uri("/books/{isbn}", isbn)
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_employee")))
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    void whenDeleteBookWithCustomerRoleThenShouldReturn403() {
        String isbn = "7373731394";
        MockMvcTester mvcTester = MockMvcTester.create(mockMvc);
        mvcTester.delete()
                .uri("/books/{isbn}", isbn)
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_customer")))
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    void whenDeleteBookNotAuthenticatedThenShouldReturn401() {
        String isbn = "7373731394";
        MockMvcTester mvcTester = MockMvcTester.create(mockMvc);
        mvcTester.delete()
                .uri("/books/{isbn}", isbn)
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.UNAUTHORIZED);
    }
}
