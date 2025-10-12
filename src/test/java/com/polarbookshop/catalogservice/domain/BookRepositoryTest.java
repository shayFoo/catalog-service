package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import com.polarbookshop.catalogservice.domain.book.Book;
import com.polarbookshop.catalogservice.domain.book.BookRepository;
import com.polarbookshop.catalogservice.persistence.book.BookEntity;
import com.polarbookshop.catalogservice.persistence.book.SpringDataBookRepositoryAdaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import({DataConfig.class, SpringDataBookRepositoryAdaptor.class})
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("integration")
public class BookRepositoryTest {
    @Autowired
    BookRepository repository;
    @Autowired
    JdbcAggregateTemplate jdbcAggregateTemplate;

    @BeforeEach
    void setUp() {
        jdbcAggregateTemplate.deleteAll(BookEntity.class);
    }

    @Test
    void mergeTest() {
        Book book = new Book("1234567890", "Title", "Author", 9.90, "publisher");
        repository.save(book);
        List<BookEntity> savedList = jdbcAggregateTemplate.findAll(BookEntity.class);
        assertThat(savedList).hasSize(1);
        BookEntity save = savedList.getFirst();

        repository.merge(save.isbn(), new Book(save.isbn(), "New Title", "New Author", 19.90, "publisher"));

        List<BookEntity> mergedList = jdbcAggregateTemplate.findAll(BookEntity.class);
        assertThat(mergedList).hasSize(1);
        BookEntity merged = mergedList.getFirst();
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
        BookEntity entity = BookEntity.of(new Book(isbn, "Title", "Author", 9.90, "publisher"));
        jdbcAggregateTemplate.insert(entity);

        Optional<Book> actual = repository.findByIsbn(isbn);

        assertThat(actual).isPresent()
                .get()
                .extracting(Book::isbn, Book::title, Book::author, Book::price)
                .containsExactly(isbn, "Title", "Author", 9.90);
    }

    @Test
    void whenCreateBookNotAuthenticatedThenNoAuditMetadata() {
        String isbn = "1234567890";
        Book book = new Book(isbn, "Title", "Author", 9.90, "publisher");

        BookEntity savedBook = jdbcAggregateTemplate.insert(BookEntity.of(book));

        assertThat(savedBook.createdBy()).isNull();
        assertThat(savedBook.lastModifiedBy()).isNull();
    }

    @Test
    @WithMockUser(username = "isabelle")
    void whenCreateBookAuthenticatedThenAuditMetadataPopulated() {
        String isbn = "1234567890";
        Book book = new Book(isbn, "Title", "Author", 9.90, "publisher");

        BookEntity savedBook = jdbcAggregateTemplate.insert(BookEntity.of(book));

        assertThat(savedBook.createdBy()).isEqualTo("isabelle");
        assertThat(savedBook.lastModifiedBy()).isEqualTo("isabelle");
    }
}
