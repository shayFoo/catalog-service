package com.polarbookshop.catalogservice.domain.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record Book(
        @NotBlank(message = "the book ISBN must be defined.")
        @Pattern(
                regexp = "^([0-9]{10}|[0-9]{13})$",
                message = "The ISBN format must be valid (either 10 or 13 digits)"
        )
        String isbn,
        @NotBlank(message = "the book title must be defined.")
        String title,
        @NotBlank(message = "the book author must be defined.")
        String author,
        @NotNull(message = "the book price must be defined.")
        @Positive(message = "the book price must be greater than zero.")
        Double price,
        String publisher
) {
}
