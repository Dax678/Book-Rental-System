package org.bookrental.bookrentalsystem.Data.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book_inventory")
public class BookInventory {
    @Id
    private Long bookId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    /**
     * Number of copies available for purchase (sale).
     */
    @Column(name = "purchase_stock_count", nullable = false)
    private Integer purchaseStockCount = 0;

    /**
     * Number of copies available for rental.
     */
    @Column(name = "rental_stock_count", nullable = false)
    private Integer rentalStockCount = 0;
}

