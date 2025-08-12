package org.bookrental.bookrentalsystem.Data.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "available")
    private boolean available;

    @Column(length = 100, nullable = false)
    private String genre = "indefinite";

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookTransaction> rentals;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(
            name = "tags",
            columnDefinition = "text[]",
            nullable = false
    )
    private String[] tags = new String[0];

    @Column(name = "rental_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal rentalPrice;

    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookInventory inventory;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookPriceHistory> priceHistory = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookPromotion> promotions = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookTransaction> transactions = new ArrayList<>();
}