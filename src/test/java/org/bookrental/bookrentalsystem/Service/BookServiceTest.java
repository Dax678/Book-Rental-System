package org.bookrental.bookrentalsystem.Service;

import org.bookrental.bookrentalsystem.Config.Exception.BookNotFoundException;
import org.bookrental.bookrentalsystem.Data.Entity.Book;
import org.bookrental.bookrentalsystem.Data.Mapper.BookMapper;
import org.bookrental.bookrentalsystem.Data.Request.BookRequest;
import org.bookrental.bookrentalsystem.Data.Response.BookResponse;
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
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldReturnBookWhenFoundById() {
        Book book = getBook();

        BookResponse response = getBookResponse();

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toBookResponse(book)).thenReturn(response);

        BookResponse result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        Mockito.when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(99L));
    }

    @Test
    void createBook_shouldSaveAndReturnBookResponse() {
        BookRequest request = getBookRequest();

        Book bookEntity = getBook();

        BookResponse response = getBookResponse();

        Mockito.when(bookMapper.toEntity(request)).thenReturn(bookEntity);
        Mockito.when(bookRepository.save(bookEntity)).thenReturn(bookEntity);
        Mockito.when(bookMapper.toBookResponse(bookEntity)).thenReturn(response);

        BookResponse result = bookService.createBook(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getTitle());

        Mockito.verify(bookRepository).save(bookEntity);
    }

    private Book getBook() {
        return Book.builder()
                .id(1L)
                .title("Clean Code")
                .build();
    }

    private BookResponse getBookResponse() {
        return BookResponse.builder()
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