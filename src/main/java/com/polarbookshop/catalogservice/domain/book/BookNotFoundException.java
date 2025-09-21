package com.polarbookshop.catalogservice.domain.book;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String isbn) {
        super("Book with ISBN %s not found.".formatted(isbn));
    }
}
