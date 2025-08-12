package org.bookrental.bookrentalsystem.Controller;

import jakarta.validation.Valid;
import org.bookrental.bookrentalsystem.Data.Request.BookPromotionRequest;
import org.bookrental.bookrentalsystem.Data.Response.BookPromotionResponse;
import org.bookrental.bookrentalsystem.Service.BookPromotionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class BookPromotionController {
    private final BookPromotionService bookPromotionService;

    public BookPromotionController(BookPromotionService bookPromotionService) {
        this.bookPromotionService = bookPromotionService;
    }

    @GetMapping
    public ResponseEntity<List<BookPromotionResponse>> getAllBookPromotions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size,
            @RequestParam(name = "status", required = false, defaultValue = "ALL") PromotionStatus status
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<BookPromotionResponse> promotions = bookPromotionService.getBookPromotionsByStatus(pageable, status);
        return ResponseEntity.ok(promotions);
    }

    @PostMapping("/save")
    public ResponseEntity<BookPromotionResponse> createBookPromotion(@Valid @RequestBody BookPromotionRequest request) {
        BookPromotionResponse response = bookPromotionService.createBookPromotion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}