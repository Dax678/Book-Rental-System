package org.bookrental.bookrentalsystem.Data.Response;

import lombok.Builder;
import lombok.Getter;
import org.bookrental.bookrentalsystem.Data.Entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class BookTransactionResponse {
    private Long id;
    private Long userId;
    private Long bookId;
    private String bookTitle;
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
    private BigDecimal operationPrice;
    private LocalDate startDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Boolean returned;
}