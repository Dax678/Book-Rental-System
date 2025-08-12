package org.bookrental.bookrentalsystem.Data.Response;

import com.beust.jcommander.internal.Nullable;
import org.bookrental.bookrentalsystem.Data.Entity.Book;

import java.math.BigDecimal;

public record BookSimpleContext(
        Book book,
        @Nullable BigDecimal rentalPromoPrice,
        @Nullable BigDecimal purchasePromoPrice
) {
}
