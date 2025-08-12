package org.bookrental.bookrentalsystem.Data.Request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BookPromotionRequest {
    @NotNull(message = "bookId must not be null")
    private Long bookId;

    @NotNull(message = "rentalPromoPrice must not be null")
    @Positive(message = "rentalPromoPrice must be greater than zero")
    private BigDecimal rentalPromoPrice;

    @NotNull(message = "purchasePromoPrice must not be null")
    @Positive(message = "purchasePromoPrice must be greater than zero")
    private BigDecimal purchasePromoPrice;

    @NotNull(message = "startDate must not be null")
    @FutureOrPresent(message = "startDate must be in the present or future")
    private LocalDateTime startDate;

    @NotNull(message = "endDate must not be null")
    @Future(message = "endDate must be in the future")
    private LocalDateTime endDate;

    @AssertTrue(message = "endDate must be equal to or after startDate")
    public boolean isValidDateRange() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }
}
