package com.polarbookshop.catalogservice.demo;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Component responsible for loading initial book data into the catalog for testing purposes.
 * This component is only active when the "testdata" profile is enabled.
 */
@Component
@Profile("testdata")
public class BookDataLoader {
    private final BookRepository repository;

    public BookDataLoader(BookRepository repository) {
        this.repository = repository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookData() {
        repository.saveAll(
                java.util.List.of(
                        new Book("9781617294945", "Spring in Action, Sixth Edition", "Craig Walls", 44.99),
                        new Book("9781617297574", "Spring Boot in Action", "Craig Walls", 39.99),
                        new Book("9780134686097", "Effective Java, Third Edition", "Joshua Bloch", 49.99),
                        new Book("9780596009205", "Head First Java, Second Edition", "Kathy Sierra, Bert Bates", 37.35)
                )
        );
    }
}
