package org.bookrental.bookrentalsystem.Data.Mapper;

import org.bookrental.bookrentalsystem.Data.Entity.BookPriceHistory;
import org.bookrental.bookrentalsystem.Data.Response.BookPriceHistoryResponse;
import org.springframework.stereotype.Component;

@Component
public class BookPriceHistoryMapper {
    public BookPriceHistoryResponse toBookPromotionResponse(BookPriceHistory bookPriceHistory) {
        return BookPriceHistoryResponse.builder()
                .priceId(bookPriceHistory.getPriceId())
                .bookId(bookPriceHistory.getBook().getId())
                .price(bookPriceHistory.getPrice())
                .validFrom(bookPriceHistory.getValidFrom())
                .validTo(bookPriceHistory.getValidTo())
                .build();
    }
}
