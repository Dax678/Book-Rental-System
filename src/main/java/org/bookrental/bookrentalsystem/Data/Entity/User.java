package org.bookrental.bookrentalsystem.Data.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "user_type", nullable = false)
    private String userType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Rental> rentals;
}
