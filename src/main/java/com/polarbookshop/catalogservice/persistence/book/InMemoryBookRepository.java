package com.polarbookshop.catalogservice.persistence.book;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBookRepository implements BookRepository {
    private static final Map<String, BookEntity> books = new ConcurrentHashMap<>();

    @Override
    public Iterable<Book> findAll() {
        return books.values()
                .stream()
                .map(BookEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return existsByIsbn(isbn)
                ? Optional.of(books.get(isbn)).map(BookEntity::toDomain)
                : Optional.empty();
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return books.containsKey(isbn);
    }

    @Override
    public Book save(Book book) {
        books.put(book.isbn(), BookEntity.of(book));
        return book;
    }

    @Override
    public void deleteByIsbn(String isbn) {
        books.remove(isbn);
    }

    @Override
    public List<Book> saveAll(List<Book> books) {
        return books.stream()
                .map(this::save)
                .toList();
    }
}
