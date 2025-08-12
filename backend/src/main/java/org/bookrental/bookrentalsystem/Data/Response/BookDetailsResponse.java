package org.bookrental.bookrentalsystem.Data.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BookDetailsResponse {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private boolean available;
    private String genre;
    private String[] tags;
    private BigDecimal rentalPrice;
    private BigDecimal purchasePrice;
    private BigDecimal rentalPromoPrice;
    private BigDecimal purchasePromoPrice;
    private Integer rentalStockCount;
    private Integer purchaseStockCount;
}