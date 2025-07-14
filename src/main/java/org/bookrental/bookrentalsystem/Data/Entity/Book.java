package org.bookrental.bookrentalsystem.Data.Entity;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Rental> rentals;
}