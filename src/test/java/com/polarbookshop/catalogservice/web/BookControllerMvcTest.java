package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@WebMvcTest(BookController.class)
class BookControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

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
}
