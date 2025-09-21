package com.polarbookshop.catalogservice.persistence.book;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SpringDataBookRepository extends ListCrudRepository<BookEntity, Long> {
    Optional<BookEntity> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Modifying
    @Transactional
    @Query("DELETE FROM book WHERE isbn = :isbn")
    void deleteByIsbn(String isbn);
}

