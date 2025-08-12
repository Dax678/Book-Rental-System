package org.bookrental.bookrentalsystem.Data.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book_promotions")
public class BookPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "rental_promo_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal rentalPromoPrice;

    @Column(name = "purchase_promo_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePromoPrice;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
}