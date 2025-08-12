package org.bookrental.bookrentalsystem.Controller;

import org.bookrental.bookrentalsystem.Data.Response.BookPriceHistoryResponse;
import org.bookrental.bookrentalsystem.Service.BookPriceHistoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prices/history")
public class BookPriceHistoryController {
    private final BookPriceHistoryService bookPriceHistoryService;

    public BookPriceHistoryController(BookPriceHistoryService bookPriceHistoryService) {
        this.bookPriceHistoryService = bookPriceHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<BookPriceHistoryResponse>> getAllBookPriceHistory(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<BookPriceHistoryResponse> bookPriceHistoryResponses = bookPriceHistoryService.getAllBookPriceHistory(pageable);
        return ResponseEntity.ok(bookPriceHistoryResponses);
    }
}
