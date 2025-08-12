package org.bookrental.bookrentalsystem.Data.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class BookRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 150, message = "Author cannot exceed 150 characters")
    private String author;

    @NotBlank(message = "ISBN is required")
    @Size(min = 10, max = 20, message = "ISBN must be between 10 and 20 characters")
    private String isbn;

    @Size(max = 50, message = "Genre cannot exceed 50 characters")
    private String genre;

    private String[] tags;

    private BigDecimal rentalPrice;

    private BigDecimal purchasePrice;
}
