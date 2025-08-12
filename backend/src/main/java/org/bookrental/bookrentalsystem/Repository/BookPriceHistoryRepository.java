package org.bookrental.bookrentalsystem.Repository;

import org.bookrental.bookrentalsystem.Data.Entity.BookPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookPriceHistoryRepository extends JpaRepository<BookPriceHistory, Long> {
}
