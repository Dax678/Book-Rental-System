package org.bookrental.bookrentalsystem.Controller;

import jakarta.validation.Valid;
import org.bookrental.bookrentalsystem.Data.Request.BookRequest;
import org.bookrental.bookrentalsystem.Data.Response.BookDetailsResponse;
import org.bookrental.bookrentalsystem.Data.Response.BookSimpleResponse;
import org.bookrental.bookrentalsystem.Service.BookService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDetailsResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookDetailsById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookSimpleResponse>> getAllBooks(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<BookSimpleResponse> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/save")
    public ResponseEntity<BookDetailsResponse> createBook(@Valid @RequestBody BookRequest request) {
        BookDetailsResponse response = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
