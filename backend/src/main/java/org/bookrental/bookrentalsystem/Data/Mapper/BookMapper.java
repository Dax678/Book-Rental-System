package org.bookrental.bookrentalsystem.Data.Mapper;

import org.bookrental.bookrentalsystem.Data.Entity.Book;
import org.bookrental.bookrentalsystem.Data.Request.BookRequest;
import org.bookrental.bookrentalsystem.Data.Response.BookDetailsContext;
import org.bookrental.bookrentalsystem.Data.Response.BookDetailsResponse;
import org.bookrental.bookrentalsystem.Data.Response.BookSimpleContext;
import org.bookrental.bookrentalsystem.Data.Response.BookSimpleResponse;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public Book toEntity(BookRequest dto) {
        return Book.builder()
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .available(true)
                .genre(dto.getGenre())
                .tags(dto.getTags())
                .rentalPrice(dto.getRentalPrice())
                .purchasePrice(dto.getPurchasePrice())
                .build();
    }

    public BookSimpleResponse toBookSimpleResponse(
            BookSimpleContext context
    ) {
        Book book = context.book();
        return BookSimpleResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .available(book.isAvailable())
                .rentalPrice(book.getRentalPrice())
                .purchasePrice(book.getPurchasePrice())
                .rentalPromoPrice(context.rentalPromoPrice())
                .purchasePromoPrice(context.purchasePromoPrice())
                .build();
    }

    public BookDetailsResponse toBookDetailsResponse(
            BookDetailsContext context
    ) {
        Book book = context.book();

        return BookDetailsResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .available(book.isAvailable())
                .genre(book.getGenre())
                .tags(book.getTags())
                .rentalPrice(book.getRentalPrice())
                .purchasePrice(book.getPurchasePrice())
                .rentalPromoPrice(context.rentalPromoPrice())
                .purchasePromoPrice(context.purchasePromoPrice())
                .rentalStockCount(context.rentalStockCount())
                .purchaseStockCount(context.purchaseStockCount())
                .build();
    }
}
