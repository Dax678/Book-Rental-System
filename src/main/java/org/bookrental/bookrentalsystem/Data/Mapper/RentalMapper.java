package org.bookrental.bookrentalsystem.Data.Mapper;

import org.bookrental.bookrentalsystem.Data.Entity.Rental;
import org.bookrental.bookrentalsystem.Data.Response.RentalHistoryResponse;
import org.bookrental.bookrentalsystem.Data.Response.RentalResponse;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {
    public RentalResponse toRentalResponse(Rental rental) {
        return RentalResponse.builder()
                .id(rental.getId())
                .userId(rental.getUser().getId())
                .bookId(rental.getBook().getId())
                .bookTitle(rental.getBook().getTitle())
                .rentedAt(rental.getRentedAt())
                .dueDate(rental.getDueDate())
                .returnDate(rental.getReturnDate())
                .returned(rental.isReturned())
                .build();
    }

    public RentalHistoryResponse toHistoryResponse(Rental rental) {
        return RentalHistoryResponse.builder()
                .rentalId(rental.getId())
                .bookId(rental.getBook().getId())
                .bookTitle(rental.getBook().getTitle())
                .userId(rental.getUser().getId())
                .userName(rental.getUser().getFullName())
                .rentedAt(rental.getRentedAt())
                .returnDate(rental.getReturnDate())
                .returned(rental.isReturned())
                .build();
    }
}
