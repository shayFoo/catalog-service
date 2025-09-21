package com.polarbookshop.catalogservice.persistence.book;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SpringDataJdbcBookRepository extends ListCrudRepository<BookEntity, Long> {
    Optional<BookEntity> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Modifying
    @Transactional
    @Query("DELETE FROM book WHERE isbn = :isbn")
    void deleteByIsbn(String isbn);

    @Transactional
    @Query("""
                        MERGE INTO book AS b
                        USING (SELECT :#{#entity.isbn} AS isbn,
                                      :#{#entity.title} AS title,
                                      :#{#entity.author} AS author,
                                      :#{#entity.publisher} AS publisher,
                                      :#{#entity.price} AS price) AS vals
                        ON b.isbn = vals.isbn
                        WHEN MATCHED THEN
                            UPDATE SET title = vals.title,
                                        author = vals.author,
                                        price = vals.price,
                                        publisher = vals.publisher,
                                        last_modified_date = CURRENT_TIMESTAMP,
                                        version = b.version + 1
                        WHEN NOT MATCHED THEN
                            INSERT (isbn, title, author, publisher, price, created_date, last_modified_date, version)
                                                        VALUES (vals.isbn,
                                                                vals.title,
                                                                vals.author,
                                                                vals.publisher,
                                                                vals.price,
                                                                CURRENT_TIMESTAMP,
                                                                CURRENT_TIMESTAMP,
                                                                0)
                        RETURNING b.id, b.isbn, b.title, b.author, b.price, b.created_date, b.last_modified_date, b.version
            """)
    BookEntity merge(BookEntity entity);
}
