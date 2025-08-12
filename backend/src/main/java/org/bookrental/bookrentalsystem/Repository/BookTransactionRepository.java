package org.bookrental.bookrentalsystem.Repository;

import org.bookrental.bookrentalsystem.Data.Entity.Book;
import org.bookrental.bookrentalsystem.Data.Entity.BookTransaction;
import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookTransactionRepository extends JpaRepository<BookTransaction, Long> {
    List<BookTransaction> findAllByUser(User user);

    List<BookTransaction> findByUserAndReturned(User user, boolean isReturned);

    List<BookTransaction> findAllByBook(Book book);

    List<BookTransaction> findByBookAndReturned(Book book, boolean isReturned);

    Optional<BookTransaction> findByUserAndId(User user, Long id);
}
