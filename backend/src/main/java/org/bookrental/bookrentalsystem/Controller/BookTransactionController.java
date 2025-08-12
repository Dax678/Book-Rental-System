package org.bookrental.bookrentalsystem.Controller;

import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.bookrental.bookrentalsystem.Data.Response.RentalHistoryResponse;
import org.bookrental.bookrentalsystem.Data.Response.BookTransactionResponse;
import org.bookrental.bookrentalsystem.Service.BookTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class BookTransactionController {

    private final BookTransactionService bookTransactionService;

    public BookTransactionController(BookTransactionService bookTransactionService) {
        this.bookTransactionService = bookTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<BookTransactionResponse>> getAllRentals() {
        return ResponseEntity.ok(bookTransactionService.getAllRentals());
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookTransactionResponse>> getLoggedUserTransactions(@AuthenticationPrincipal User currentUser) {
        List<BookTransactionResponse> bookTransactionResponse = bookTransactionService.getLoggedUserTransactions(currentUser);
        return ResponseEntity.ok(bookTransactionResponse);
    }

    @PostMapping("/rent")
    public ResponseEntity<BookTransactionResponse> rentBook(@RequestParam Long userId, @RequestParam Long bookId) {
        BookTransactionResponse rental = bookTransactionService.rentBook(userId, bookId);
        return ResponseEntity.ok(rental);
    }

    @PostMapping("/return/{rentalId}")
    public ResponseEntity<BookTransactionResponse> returnBook(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long rentalId) {
        BookTransactionResponse rental = bookTransactionService.returnBook(currentUser, rentalId);
        return ResponseEntity.ok(rental);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalHistoryResponse>> getUserHistory(@PathVariable Long userId,
                                                                      @RequestParam(required = false, defaultValue = "ALL") RentalFilter filter) {
        return ResponseEntity.ok(bookTransactionService.getUserRentalHistory(userId, filter));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<RentalHistoryResponse>> getBookHistory(@PathVariable Long bookId,
                                                                      @RequestParam(required = false, defaultValue = "ALL") RentalFilter filter) {
        return ResponseEntity.ok(bookTransactionService.getBookRentalHistory(bookId, filter));
    }
}
