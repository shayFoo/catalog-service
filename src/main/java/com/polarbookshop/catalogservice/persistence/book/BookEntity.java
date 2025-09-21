package com.polarbookshop.catalogservice.persistence.book;

import com.polarbookshop.catalogservice.domain.Book;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.lang.NonNull;

public record BookEntity(
        @Id
        Long id,
        @NonNull
        String isbn,
        @NonNull
        String title,
        @NonNull
        String author,
        double price,
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
                book.price(),
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
                this.price
        );
    }

}
