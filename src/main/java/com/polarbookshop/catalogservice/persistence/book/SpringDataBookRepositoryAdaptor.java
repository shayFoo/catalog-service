package com.polarbookshop.catalogservice.persistence.book;

import com.polarbookshop.catalogservice.domain.book.Book;
import com.polarbookshop.catalogservice.domain.book.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SpringDataBookRepositoryAdaptor implements BookRepository {
    private final SpringDataBookRepository springDataBookRepository;

    public SpringDataBookRepositoryAdaptor(SpringDataBookRepository springDataBookRepository) {
        this.springDataBookRepository = springDataBookRepository;
    }

    @Override
    public List<Book> findAll() {
        return toDomainList(springDataBookRepository.findAll());
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return springDataBookRepository.findByIsbn(isbn)
                .map(BookEntity::toDomain);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return springDataBookRepository.existsByIsbn(isbn);
    }

    @Override
    public Book save(Book book) {
        return springDataBookRepository.save(BookEntity.of(book))
                .toDomain();
    }

    @Override
    public void deleteByIsbn(String isbn) {
        springDataBookRepository.deleteByIsbn(isbn);
    }

    @Override
    public List<Book> saveAll(List<Book> books) {
        List<BookEntity> entities = books.stream()
                .map(BookEntity::of)
                .toList();
        return toDomainList(springDataBookRepository.saveAll(entities));
    }

    private List<Book> toDomainList(List<BookEntity> entities) {
        return entities.stream()
                .map(BookEntity::toDomain)
                .toList();
    }
}
