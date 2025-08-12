package org.bookrental.bookrentalsystem.Data.Response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class BookPriceHistoryResponse {
    @NotNull(message = "priceId must not be null")
    private Long priceId;

    @NotNull(message = "bookId must not be null")
    private Long bookId;

    @NotNull(message = "price must not be null")
    @Positive(message = "price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "validFrom must not be null")
    @PastOrPresent(message = "validFrom must be in the past or present")
    private LocalDateTime validFrom;

    private LocalDateTime validTo;
}
