package org.bookrental.bookrentalsystem.Data.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class BookPromotionResponse {
    private Long promoId;
    private Long bookId;
    private BigDecimal rentalPromoPrice;
    private BigDecimal purchasePromoPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
