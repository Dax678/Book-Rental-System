package org.bookrental.bookrentalsystem.Repository;

import org.bookrental.bookrentalsystem.Data.Entity.Book;
import org.bookrental.bookrentalsystem.Data.Entity.Rental;
import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findAllByUser(User user);

    List<Rental> findByUserAndReturned(User user, boolean isReturned);

    List<Rental> findAllByBook(Book book);

    List<Rental> findByBookAndReturned(Book book, boolean isReturned);
}
