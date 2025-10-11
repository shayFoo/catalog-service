package com.polarbookshop.catalogservice.persistence.book;

import com.polarbookshop.catalogservice.domain.book.Book;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Table(name = "book")
public record BookEntity(
        @Id
        Long id,
        @NonNull
        String isbn,
        @NonNull
        String title,
        @NonNull
        String author,
        @NonNull
        String publisher,
        double price,
        @CreatedDate
        LocalDateTime createdDate,
        @LastModifiedDate
        LocalDateTime lastModifiedDate,
        @CreatedBy
        String createdBy,
        @LastModifiedBy
        String lastModifiedBy,
        @Version
        int version
) {
    /**
     * BookモデルをBookEntityモデルに変換する
     * <br>
     * idがnull, versionが0であることは、新規作成されるエンティティであることを示す
     */
    public static BookEntity of(Book book) {
        return new BookEntity(
                null,
                book.isbn(),
                book.title(),
                book.author(),
                book.publisher(),
                book.price(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                0
        );
    }

    /**
     * BookEntityモデルをBookモデルに変換する
     */
    public Book toDomain() {
        return new Book(
                this.isbn,
                this.title,
                this.author,
                this.price,
                this.publisher
        );
    }

    public BookEntity updateWith(Book book) {
        return new BookEntity(
                this.id,
                this.isbn,
                book.title(),
                book.author(),
                book.publisher(),
                book.price(),
                this.createdDate,
                LocalDateTime.now(),
                this.createdBy,
                this.lastModifiedBy,
                this.version
        );
    }

}
