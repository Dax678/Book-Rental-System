package org.bookrental.bookrentalsystem.Service;

import org.bookrental.bookrentalsystem.Config.Exception.BookNotFoundException;
import org.bookrental.bookrentalsystem.Data.Entity.Book;
import org.bookrental.bookrentalsystem.Data.Mapper.BookMapper;
import org.bookrental.bookrentalsystem.Data.Request.BookRequest;
import org.bookrental.bookrentalsystem.Data.Response.BookDetailsContext;
import org.bookrental.bookrentalsystem.Data.Response.BookDetailsResponse;
import org.bookrental.bookrentalsystem.Repository.BookInventoryRepository;
import org.bookrental.bookrentalsystem.Repository.BookPromotionRepository;
import org.bookrental.bookrentalsystem.Repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookPromotionRepository bookPromotionRepository;

    @Mock
    private BookInventoryRepository bookInventoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

//    @Test
//    void shouldReturnBookWhenFoundById() {
//        Book book = getBook();
//
//        BookDetailsResponse response = getBookResponse();
//        BookDetailsContext bookDetailsContext = new BookDetailsContext(book, null, null, 5, 2);
//
//        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
//        Mockito.when(bookInventoryRepository.findByBookId(1L)).thenReturn();
//        Mockito.when(bookMapper.toBookDetailsResponse(bookDetailsContext)).thenReturn(response);
//
//        BookDetailsResponse result = bookService.getBookDetailsById(1L);
//
//        assertNotNull(result);
//        assertEquals("Clean Code", result.getTitle());
//    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        Mockito.when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookDetailsById(99L));
    }

    @Test
    void createBook_shouldSaveAndReturnBookResponse() {
        BookRequest request = getBookRequest();

        Book book = getBook();

        BookDetailsResponse response = getBookResponse();
        BookDetailsContext bookDetailsContext = new BookDetailsContext(book, null, null, 5, 2);

        Mockito.when(bookMapper.toEntity(request)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toBookDetailsResponse(bookDetailsContext)).thenReturn(response);

        BookDetailsResponse result = bookService.createBook(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getTitle());

        Mockito.verify(bookRepository).save(book);
    }

    private Book getBook() {
        return Book.builder()
                .id(1L)
                .title("Clean Code")
                .build();
    }

    private BookDetailsResponse getBookResponse() {
        return BookDetailsResponse.builder()
                .id(1L)
                .title("Clean Code")
                .build();
    }

    private BookRequest getBookRequest() {
        return BookRequest.builder()
                .isbn("1234567890")
                .title("Clean Code")
                .author("Robert C. Martin")
                .build();
    }
}