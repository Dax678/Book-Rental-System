package org.bookrental.bookrentalsystem.Repository;

import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByNameContainingIgnoreCase(String name);
}
