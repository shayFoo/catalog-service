package com.polarbookshop.catalogservice.domain.book;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String isbn) {
        super("Book with ISBN %s already exists.".formatted(isbn));
    }
}
