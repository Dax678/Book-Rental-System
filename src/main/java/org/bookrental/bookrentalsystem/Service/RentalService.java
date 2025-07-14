package org.bookrental.bookrentalsystem.Service;

import org.bookrental.bookrentalsystem.Config.Exception.BookNotFoundException;
import org.bookrental.bookrentalsystem.Config.Exception.RentalNotFoundException;
import org.bookrental.bookrentalsystem.Config.Exception.UserNotFoundException;
import org.bookrental.bookrentalsystem.Controller.RentalFilter;
import org.bookrental.bookrentalsystem.Data.Entity.Book;
import org.bookrental.bookrentalsystem.Data.Entity.Rental;
import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.bookrental.bookrentalsystem.Data.Mapper.RentalMapper;
import org.bookrental.bookrentalsystem.Data.Response.RentalHistoryResponse;
import org.bookrental.bookrentalsystem.Data.Response.RentalResponse;
import org.bookrental.bookrentalsystem.Notification.NotificationManager;
import org.bookrental.bookrentalsystem.Repository.BookRepository;
import org.bookrental.bookrentalsystem.Repository.RentalRepository;
import org.bookrental.bookrentalsystem.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RentalMapper rentalMapper;
    private final NotificationManager notificationManager;

    public RentalService(RentalRepository rentalRepository, BookRepository bookRepository, UserRepository userRepository, RentalMapper rentalMapper, NotificationManager notificationManager) {
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.rentalMapper = rentalMapper;
        this.notificationManager = notificationManager;
    }

    public RentalResponse rentBook(Long userId, Long bookId) {
        User user = getUserOrThrow(userId);
        Book book = getBookOrThrow(bookId);

        validateBookAvailable(book);

        Rental rental = createRental(user, book);
        updateBookAvailability(book, false);

        Rental saved = rentalRepository.save(rental);

        notificationManager.notifyAll(saved.getUser(),
                "email.rent.confirmation",
                rental.getBook().getTitle());

        return rentalMapper.toRentalResponse(saved);
    }

    private void validateBookAvailable(Book book) {
        if(!book.isAvailable())
            throw new IllegalStateException("Book is already rented");
    }

    private Rental createRental(User user, Book book) {
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setBook(book);
        rental.setRentedAt(LocalDate.now());
        rental.setDueDate(LocalDate.now().plusDays(14));
        rental.setReturned(false);
        return rental;
    }

    public RentalResponse returnBook(Long rentalId) {
        Rental rental = getRentalOrThrow(rentalId);
        validateNotAlreadyReturned(rental);

        rental.setReturned(true);
        rental.setReturnDate(LocalDate.now());
        updateBookAvailability(rental.getBook(), true);

        Rental saved = rentalRepository.save(rental);

        notificationManager.notifyAll(saved.getUser(),
                "email.return.confirmation",
                rental.getBook().getTitle());

        return rentalMapper.toRentalResponse(saved);
    }

    private void updateBookAvailability(Book book, boolean available) {
        book.setAvailable(available);
        bookRepository.save(book);
    }

    private void validateNotAlreadyReturned(Rental rental) {
        if (rental.isReturned()) {
            throw new IllegalStateException("Book has already been returned");
        }
    }

    public List<RentalResponse> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();

        return rentals.stream()
                .map(rentalMapper::toRentalResponse)
                .toList();
    }

    public List<RentalHistoryResponse> getUserRentalHistory(Long userId, RentalFilter filter) {
        User user = getUserOrThrow(userId);
        List<Rental> rentals = getRentalsByFilter(user, filter);

        return rentals.stream()
                .map(rentalMapper::toHistoryResponse)
                .toList();
    }

    private List<Rental> getRentalsByFilter(User user, RentalFilter filter) {
        return switch (filter) {
            case ALL -> rentalRepository.findAllByUser(user);
            case ACTIVE -> rentalRepository.findByUserAndReturned(user, false);
            case RETURNED -> rentalRepository.findByUserAndReturned(user, true);
        };
    }

    public List<RentalHistoryResponse> getBookRentalHistory(Long bookId, RentalFilter filter) {
        Book book = getBookOrThrow(bookId);
        List<Rental> rentals = getRentalsByFilter(book, filter);

        return rentals.stream()
                .map(rentalMapper::toHistoryResponse)
                .toList();
    }

    private List<Rental> getRentalsByFilter(Book book, RentalFilter filter) {
        return switch (filter) {
            case ALL -> rentalRepository.findAllByBook(book);
            case ACTIVE -> rentalRepository.findByBookAndReturned(book, false);
            case RETURNED -> rentalRepository.findByBookAndReturned(book, true);
        };
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private Book getBookOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    private Rental getRentalOrThrow(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException("Rental not found"));
    }
}
