package com.polarbookshop.catalogservice.persistence.book;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SpringDataJdbcBookRepository implements BookRepository {
    private final JdbcBookRepository jdbcBookRepository;

    public SpringDataJdbcBookRepository(JdbcBookRepository jdbcBookRepository) {
        this.jdbcBookRepository = jdbcBookRepository;
    }

    @Override
    public List<Book> findAll() {
        return toDomainList(jdbcBookRepository.findAll());
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return jdbcBookRepository.findByIsbn(isbn)
                .map(BookEntity::toDomain);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return jdbcBookRepository.existsByIsbn(isbn);
    }

    @Override
    public Book save(Book book) {
        return jdbcBookRepository.save(BookEntity.of(book))
                .toDomain();
    }

    @Override
    public void deleteByIsbn(String isbn) {
        jdbcBookRepository.deleteByIsbn(isbn);
    }

    @Override
    public List<Book> saveAll(List<Book> books) {
        List<BookEntity> entities = books.stream()
                .map(BookEntity::of)
                .toList();
        return toDomainList(jdbcBookRepository.saveAll(entities));
    }

    private List<Book> toDomainList(List<BookEntity> entities) {
        return entities.stream()
                .map(BookEntity::toDomain)
                .toList();
    }
}
