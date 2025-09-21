package com.polarbookshop.catalogservice.persistance;

import com.polarbookshop.catalogservice.domain.book.Book;
import com.polarbookshop.catalogservice.persistence.book.BookEntity;
import com.polarbookshop.catalogservice.persistence.book.SpringDataJdbcBookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpringDataJdbcBookRepositoryTest {
    @Autowired
    SpringDataJdbcBookRepository repository;

    @Test
    void mergeTest() {
        BookEntity entity = BookEntity.of(new Book("1234567890", "Title", "Author", 9.90));
        BookEntity save = repository.merge(entity);
        BookEntity merged = repository.merge(BookEntity.of(new Book(save.isbn(), "New Title", "New Author", 19.90)));
        assertThat(merged.title()).isEqualTo("New Title");
        System.out.println(merged);
        System.out.println(save);
    }
}
