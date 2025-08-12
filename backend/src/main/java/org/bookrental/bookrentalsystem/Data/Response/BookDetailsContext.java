package org.bookrental.bookrentalsystem.Data.Response;

import org.bookrental.bookrentalsystem.Data.Entity.Book;

import java.math.BigDecimal;

public record BookDetailsContext(
        Book book,
        BigDecimal rentalPromoPrice,
        BigDecimal purchasePromoPrice,
        int rentalStockCount,
        int purchaseStockCount
) {
}
