package com.polarbookshop.catalogservice.domain;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Iterable<Book> findAll();

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    Book save(Book book);

    void deleteByIsbn(String isbn);

    List<Book> saveAll(List<Book> books);
}
