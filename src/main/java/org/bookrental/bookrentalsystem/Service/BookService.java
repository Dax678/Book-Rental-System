package org.bookrental.bookrentalsystem.Service;

import org.bookrental.bookrentalsystem.Config.Exception.BookNotFoundException;
import org.bookrental.bookrentalsystem.Data.Entity.Book;
import org.bookrental.bookrentalsystem.Data.Mapper.BookMapper;
import org.bookrental.bookrentalsystem.Data.Request.BookRequest;
import org.bookrental.bookrentalsystem.Data.Response.BookResponse;
import org.bookrental.bookrentalsystem.Repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public BookResponse getBookById(Long bookId) {
        Book book = getBookOrThrow(bookId);

        return bookMapper.toBookResponse(book);
    }

    public BookResponse createBook(BookRequest request) {
        Book book = bookMapper.toEntity(request);
        Book saved = bookRepository.save(book);

        return bookMapper.toBookResponse(saved);
    }

    private Book getBookOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
    }
}
