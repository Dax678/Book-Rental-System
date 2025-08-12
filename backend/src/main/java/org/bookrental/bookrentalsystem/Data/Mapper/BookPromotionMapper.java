package org.bookrental.bookrentalsystem.Data.Mapper;

import org.bookrental.bookrentalsystem.Config.Exception.BookPromotionNotFoundException;
import org.bookrental.bookrentalsystem.Data.Entity.Book;
import org.bookrental.bookrentalsystem.Data.Entity.BookPromotion;
import org.bookrental.bookrentalsystem.Data.Request.BookPromotionRequest;
import org.bookrental.bookrentalsystem.Data.Response.BookPromotionResponse;
import org.bookrental.bookrentalsystem.Repository.BookRepository;
import org.springframework.stereotype.Component;

@Component
public class BookPromotionMapper {
    private final BookRepository bookRepository;

    public BookPromotionMapper(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookPromotion toEntity(BookPromotionRequest dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new BookPromotionNotFoundException("Book Promotion not found"));

        return BookPromotion.builder()
                .book(book)
                .rentalPromoPrice(dto.getRentalPromoPrice())
                .purchasePromoPrice(dto.getPurchasePromoPrice())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }

    public BookPromotionResponse toBookPromotionResponse(BookPromotion bookPromotion) {
        return BookPromotionResponse.builder()
                .promoId(bookPromotion.getPromoId())
                .bookId(bookPromotion.getBook().getId())
                .rentalPromoPrice(bookPromotion.getRentalPromoPrice())
                .purchasePromoPrice(bookPromotion.getPurchasePromoPrice())
                .startDate(bookPromotion.getStartDate())
                .endDate(bookPromotion.getEndDate())
                .build();
    }
}
