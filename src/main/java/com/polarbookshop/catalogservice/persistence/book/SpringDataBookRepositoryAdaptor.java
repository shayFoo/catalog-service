package com.polarbookshop.catalogservice.persistence.book;

import com.polarbookshop.catalogservice.domain.book.Book;
import com.polarbookshop.catalogservice.domain.book.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SpringDataBookRepositoryAdaptor implements BookRepository {
    private final SpringDataJdbcBookRepository springDataJdbcBookRepository;

    public SpringDataBookRepositoryAdaptor(SpringDataJdbcBookRepository springDataJdbcBookRepository) {
        this.springDataJdbcBookRepository = springDataJdbcBookRepository;
    }

    @Override
    public List<Book> findAll() {
        return toDomainList(springDataJdbcBookRepository.findAll());
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return springDataJdbcBookRepository.findByIsbn(isbn)
                .map(BookEntity::toDomain);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return springDataJdbcBookRepository.existsByIsbn(isbn);
    }

    @Override
    public Book save(Book book) {
        return springDataJdbcBookRepository.save(BookEntity.of(book))
                .toDomain();
    }

    @Override
    public void deleteByIsbn(String isbn) {
        springDataJdbcBookRepository.deleteByIsbn(isbn);
    }

    @Override
    public Book merge(String isbn, Book book) {
        Optional<BookEntity> maybeEntity = springDataJdbcBookRepository.findByIsbn(isbn);
        return maybeEntity.map(bookEntity -> bookEntity.updateWith(book))
                .map(springDataJdbcBookRepository::save)
                .orElseGet(() -> springDataJdbcBookRepository.save(BookEntity.of(book)))
                .toDomain();
    }

    private List<Book> toDomainList(List<BookEntity> entities) {
        return entities.stream()
                .map(BookEntity::toDomain)
                .toList();
    }
}
