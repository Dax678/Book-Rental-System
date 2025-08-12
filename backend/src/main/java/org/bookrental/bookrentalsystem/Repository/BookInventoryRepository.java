package org.bookrental.bookrentalsystem.Repository;

import org.bookrental.bookrentalsystem.Data.Entity.BookInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookInventoryRepository extends JpaRepository<BookInventory, Long> {
    Optional<BookInventory> findByBookId(Long bookId);
}
