package com.polarbookshop.catalogservice.persistence.book;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface JdbcBookRepository extends ListCrudRepository<BookEntity, Long> {
    Optional<BookEntity> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Modifying
    @Query("DELETE FROM book WHERE isbn = :isbn")
    void deleteByIsbn(String isbn);
}

