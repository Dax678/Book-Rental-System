package org.bookrental.bookrentalsystem.Data.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BookSimpleResponse {
    private Long id;
    private String title;
    private String author;
    private Boolean available;
    private BigDecimal rentalPrice;
    private BigDecimal purchasePrice;
    private BigDecimal rentalPromoPrice;
    private BigDecimal purchasePromoPrice;
}
