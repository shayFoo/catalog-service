package com.polarbookshop.catalogservice.persistance;

import com.polarbookshop.catalogservice.config.DataConfig;
import com.polarbookshop.catalogservice.domain.book.Book;
import com.polarbookshop.catalogservice.persistence.book.BookEntity;
import com.polarbookshop.catalogservice.persistence.book.SpringDataJdbcBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("integration")
public class SpringDataJdbcBookRepositoryTest {
    @Autowired
    SpringDataJdbcBookRepository repository;

    @Autowired
    JdbcAggregateTemplate jdbcAggregateTemplate;

    @BeforeEach
    void setUp() {
        jdbcAggregateTemplate.deleteAll(BookEntity.class);
    }

    @Test
    void mergeTest() {
        BookEntity entity = BookEntity.of(new Book("1234567890", "Title", "Author", 9.90));
        BookEntity save = repository.merge(entity);

        BookEntity merged = repository.merge(BookEntity.of(new Book(save.isbn(), "New Title", "New Author", 19.90)));

        assertThat(merged.title()).isEqualTo("New Title");
        assertThat(merged.author()).isEqualTo("New Author");
        assertThat(merged.price()).isEqualTo(19.90);
        assertThat(merged.createdDate()).isEqualTo(save.createdDate());
        assertThat(merged.lastModifiedDate()).isAfterOrEqualTo(save.lastModifiedDate());
        assertThat(merged.version()).isEqualTo(save.version() + 1);
    }

    @Test
    void findBookByIsbnIfExistTest() {
        String isbn = "1234567890";
        BookEntity entity = BookEntity.of(new Book(isbn, "Title", "Author", 9.90));
        jdbcAggregateTemplate.insert(entity);

        Optional<BookEntity> actual = repository.findByIsbn(isbn);

        assertThat(actual).isPresent()
                .get()
                .extracting(BookEntity::isbn, BookEntity::title, BookEntity::author, BookEntity::price)
                .containsExactly(isbn, "Title", "Author", 9.90);
    }
}
