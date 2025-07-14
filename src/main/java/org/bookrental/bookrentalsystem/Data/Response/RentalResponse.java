package org.bookrental.bookrentalsystem.Data.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class RentalResponse {
    private Long id;
    private Long userId;
    private Long bookId;
    private String bookTitle;
    private LocalDate rentedAt;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean returned;
}