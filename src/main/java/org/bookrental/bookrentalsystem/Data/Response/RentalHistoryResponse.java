package org.bookrental.bookrentalsystem.Data.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class RentalHistoryResponse {
    private Long rentalId;
    private Long bookId;
    private String bookTitle;
    private Long userId;
    private String userName;
    private LocalDate rentedAt;
    private LocalDate returnDate;
    private boolean returned;
}
