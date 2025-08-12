package org.bookrental.bookrentalsystem.Service;

import org.bookrental.bookrentalsystem.Config.Exception.BookNotFoundException;
import org.bookrental.bookrentalsystem.Config.Exception.RentalNotFoundException;
import org.bookrental.bookrentalsystem.Config.Exception.UserNotFoundException;
import org.bookrental.bookrentalsystem.Controller.RentalFilter;
import org.bookrental.bookrentalsystem.Data.Entity.Book;
import org.bookrental.bookrentalsystem.Data.Entity.BookTransaction;
import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.bookrental.bookrentalsystem.Data.Mapper.BookTransactionMapper;
import org.bookrental.bookrentalsystem.Data.Response.RentalHistoryResponse;
import org.bookrental.bookrentalsystem.Data.Response.BookTransactionResponse;
import org.bookrental.bookrentalsystem.Notification.NotificationManager;
import org.bookrental.bookrentalsystem.Repository.BookRepository;
import org.bookrental.bookrentalsystem.Repository.BookTransactionRepository;
import org.bookrental.bookrentalsystem.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookTransactionService {
    private final BookTransactionRepository bookTransactionRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookTransactionMapper rentalMapper;
    private final NotificationManager notificationManager;

    public BookTransactionService(BookTransactionRepository bookTransactionRepository, BookRepository bookRepository, UserRepository userRepository, BookTransactionMapper rentalMapper, NotificationManager notificationManager) {
        this.bookTransactionRepository = bookTransactionRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.rentalMapper = rentalMapper;
        this.notificationManager = notificationManager;
    }

    public List<BookTransactionResponse> getLoggedUserTransactions(User currentUser) {
        List<BookTransaction> bookTransactions = bookTransactionRepository.findAllByUser(currentUser);
        return bookTransactions.stream().map(rentalMapper::toRentalResponse).toList();
    }

    public BookTransactionResponse rentBook(Long userId, Long bookId) {
        User user = getUserOrThrow(userId);
        Book book = getBookOrThrow(bookId);

        validateBookAvailable(book);

        BookTransaction rental = createRental(user, book);
        updateBookAvailability(book, false);

        BookTransaction saved = bookTransactionRepository.save(rental);

        notificationManager.notifyAll(saved.getUser(),
                "email.rent.confirmation",
                rental.getBook().getTitle());

        return rentalMapper.toRentalResponse(saved);
    }

    public BookTransactionResponse returnBook(User currentUser, Long rentalId) {
        BookTransaction rental = getRentalOrThrow(currentUser, rentalId);
        validateNotAlreadyReturned(rental);

        rental.setReturned(true);
        rental.setReturnDate(LocalDate.now());
        updateBookAvailability(rental.getBook(), true);

        BookTransaction saved = bookTransactionRepository.save(rental);

        notificationManager.notifyAll(saved.getUser(),
                "email.return.confirmation",
                rental.getBook().getTitle());

        return rentalMapper.toRentalResponse(saved);
    }

    public List<BookTransactionResponse> getAllRentals() {
        List<BookTransaction> rentals = bookTransactionRepository.findAll();

        return rentals.stream()
                .map(rentalMapper::toRentalResponse)
                .toList();
    }

    public List<RentalHistoryResponse> getUserRentalHistory(Long userId, RentalFilter filter) {
        User user = getUserOrThrow(userId);
        List<BookTransaction> rentals = getRentalsByFilter(user, filter);

        return rentals.stream()
                .map(rentalMapper::toHistoryResponse)
                .toList();
    }

    public List<RentalHistoryResponse> getBookRentalHistory(Long bookId, RentalFilter filter) {
        Book book = getBookOrThrow(bookId);
        List<BookTransaction> rentals = getRentalsByFilter(book, filter);

        return rentals.stream()
                .map(rentalMapper::toHistoryResponse)
                .toList();
    }

    private void validateBookAvailable(Book book) {
        if(!book.isAvailable())
            throw new IllegalStateException("Book is already rented");
    }

    private BookTransaction createRental(User user, Book book) {
        BookTransaction rental = new BookTransaction();
        rental.setUser(user);
        rental.setBook(book);
        rental.setTransactionDate(LocalDateTime.now());
        rental.setDueDate(LocalDate.now().plusDays(14));
        rental.setReturned(false);
        return rental;
    }

    private void updateBookAvailability(Book book, boolean available) {
        book.setAvailable(available);
        bookRepository.save(book);
    }

    private void validateNotAlreadyReturned(BookTransaction rental) {
        if (rental.getReturned()) {
            throw new IllegalStateException("Book has already been returned");
        }
    }

    private List<BookTransaction> getRentalsByFilter(User user, RentalFilter filter) {
        return switch (filter) {
            case ALL -> bookTransactionRepository.findAllByUser(user);
            case ACTIVE -> bookTransactionRepository.findByUserAndReturned(user, false);
            case RETURNED -> bookTransactionRepository.findByUserAndReturned(user, true);
        };
    }

    private List<BookTransaction> getRentalsByFilter(Book book, RentalFilter filter) {
        return switch (filter) {
            case ALL -> bookTransactionRepository.findAllByBook(book);
            case ACTIVE -> bookTransactionRepository.findByBookAndReturned(book, false);
            case RETURNED -> bookTransactionRepository.findByBookAndReturned(book, true);
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

    private BookTransaction getRentalOrThrow(User currentUser, Long rentalId) {
        return bookTransactionRepository.findByUserAndId(currentUser, rentalId)
                .orElseThrow(() -> new RentalNotFoundException("Rental not found"));
    }
}
