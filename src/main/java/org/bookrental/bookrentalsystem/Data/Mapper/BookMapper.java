package org.bookrental.bookrentalsystem.Data.Mapper;

import org.bookrental.bookrentalsystem.Data.Entity.Book;
import org.bookrental.bookrentalsystem.Data.Request.BookRequest;
import org.bookrental.bookrentalsystem.Data.Response.BookResponse;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public Book toEntity(BookRequest dto) {
        return Book.builder()
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .available(true)
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .available(book.isAvailable())
                .build();
    }
}
