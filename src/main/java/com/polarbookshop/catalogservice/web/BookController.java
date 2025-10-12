package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.book.Book;
import com.polarbookshop.catalogservice.domain.book.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/books")
public class BookController {
    private final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Iterable<Book> viewBookList() {
        log.info("Fetching the list of books at {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return bookService.viewBookList();
    }

    @GetMapping("/{isbn}")
    public Book viewBookDetails(@PathVariable String isbn) {
        return bookService.viewBookDetails(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBookToCatalog(@Valid @RequestBody Book book, @AuthenticationPrincipal Jwt jwt) {
        log.info("Book {} is added by {}", book, jwt.getClaimAsString("preferred_username"));
        return bookService.addBookToCatalog(book);
    }

    @DeleteMapping("/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookFromCatalog(@PathVariable String isbn) {
        bookService.removeBookFromCatalog(isbn);
    }

    @PutMapping("/{isbn}")
    public Book editBookDetails(@PathVariable String isbn, @Valid @RequestBody Book book) {
        return bookService.editBookDetails(isbn, book);
    }
}
