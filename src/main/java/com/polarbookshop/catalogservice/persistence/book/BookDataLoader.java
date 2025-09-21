package com.polarbookshop.catalogservice.persistence.book;

import com.polarbookshop.catalogservice.domain.book.Book;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component responsible for loading initial book data into the catalog for testing purposes.
 * This component is only active when the "testdata" profile is enabled.
 */
@Component
@Profile("testdata")
public class BookDataLoader {
    private final SpringDataJdbcBookRepository springDataJdbcBookRepository;

    public BookDataLoader(SpringDataJdbcBookRepository springDataJdbcBookRepository) {
        this.springDataJdbcBookRepository = springDataJdbcBookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookData() {
        if (springDataJdbcBookRepository.count() > 0) {
            return; // Data already loaded
        }
        springDataJdbcBookRepository.deleteAll();
        List<Book> books = List.of(
                new Book("9781617294945", "Spring in Action, Sixth Edition", "Craig Walls", 44.99),
                new Book("9781617297574", "Spring Boot in Action", "Craig Walls", 39.99),
                new Book("9780134686097", "Effective Java, Third Edition", "Joshua Bloch", 49.99),
                new Book("9780596009205", "Head First Java, Second Edition", "Kathy Sierra, Bert Bates", 37.35)
        );
        springDataJdbcBookRepository.saveAll(books.stream()
                .map(BookEntity::of)
                .toList());
    }
}
