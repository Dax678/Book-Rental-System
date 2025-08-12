package org.bookrental.bookrentalsystem.Data.Mapper;

import org.bookrental.bookrentalsystem.Data.Entity.BookTransaction;
import org.bookrental.bookrentalsystem.Data.Response.RentalHistoryResponse;
import org.bookrental.bookrentalsystem.Data.Response.BookTransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class BookTransactionMapper {
    public BookTransactionResponse toRentalResponse(BookTransaction rental) {
        return BookTransactionResponse.builder()
                .id(rental.getId())
                .userId(rental.getUser().getId())
                .bookId(rental.getBook().getId())
                .bookTitle(rental.getBook().getTitle())
                .transactionType(rental.getTransactionType())
                .transactionDate(rental.getTransactionDate())
                .operationPrice(rental.getOperationPrice())
                .startDate(rental.getStartDate())
                .dueDate(rental.getDueDate())
                .returnDate(rental.getReturnDate())
                .returned(rental.getReturned())
                .build();
    }

    public RentalHistoryResponse toHistoryResponse(BookTransaction rental) {
        return RentalHistoryResponse.builder()
                .rentalId(rental.getId())
                .bookId(rental.getBook().getId())
                .bookTitle(rental.getBook().getTitle())
                .userId(rental.getUser().getId())
                .userName(rental.getUser().getFullName())
                .transactionType(rental.getTransactionType())
                .transactionDate(rental.getTransactionDate())
                .operationPrice(rental.getOperationPrice())
                .startDate(rental.getStartDate())
                .dueDate(rental.getDueDate())
                .returnDate(rental.getReturnDate())
                .returned(rental.getReturned())
                .build();
    }
}
