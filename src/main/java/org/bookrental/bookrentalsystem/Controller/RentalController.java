package org.bookrental.bookrentalsystem.Controller;

import org.bookrental.bookrentalsystem.Data.Response.RentalHistoryResponse;
import org.bookrental.bookrentalsystem.Data.Response.RentalResponse;
import org.bookrental.bookrentalsystem.Service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/rent")
    public ResponseEntity<RentalResponse> rentBook(@RequestParam Long userId, @RequestParam Long bookId) {
        RentalResponse rental = rentalService.rentBook(userId, bookId);
        return ResponseEntity.ok(rental);
    }

    @PostMapping("/return/{rentalId}")
    public ResponseEntity<RentalResponse> returnBook(@PathVariable Long rentalId) {
        RentalResponse rental = rentalService.returnBook(rentalId);
        return ResponseEntity.ok(rental);
    }

    @GetMapping
    public ResponseEntity<List<RentalResponse>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalHistoryResponse>> getUserHistory(@PathVariable Long userId,
                                                                      @RequestParam(required = false, defaultValue = "ALL") RentalFilter filter) {
        return ResponseEntity.ok(rentalService.getUserRentalHistory(userId, filter));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<RentalHistoryResponse>> getBookHistory(@PathVariable Long bookId,
                                                                      @RequestParam(required = false, defaultValue = "ALL") RentalFilter filter) {
        return ResponseEntity.ok(rentalService.getBookRentalHistory(bookId, filter));
    }
}
