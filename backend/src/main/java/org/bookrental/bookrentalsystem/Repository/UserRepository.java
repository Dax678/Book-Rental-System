package org.bookrental.bookrentalsystem.Repository;

import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByFullNameContainingIgnoreCase(String name);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
