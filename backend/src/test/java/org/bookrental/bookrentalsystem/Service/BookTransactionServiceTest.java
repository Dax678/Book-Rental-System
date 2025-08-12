package org.bookrental.bookrentalsystem.Service;

import org.bookrental.bookrentalsystem.Config.Exception.BookNotFoundException;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookTransactionServiceTest {

    @Mock
    private BookTransactionRepository bookTransactionRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookTransactionMapper rentalMapper;

    @Mock
    private NotificationManager notificationManager;

    @InjectMocks
    private BookTransactionService bookTransactionService;

    @Test
    void shouldRentBookSuccessfully() {
        User user = getUser();

        Book book = getBook();

        BookTransaction rental = getRental(user, book);

        BookTransactionResponse response = getRentalResponse(rental);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        Mockito.when(bookTransactionRepository.save(Mockito.any(BookTransaction.class))).thenReturn(rental);
        Mockito.when(rentalMapper.toRentalResponse(rental)).thenReturn(response);

        BookTransactionResponse result = bookTransactionService.rentBook(user.getId(), book.getId());

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getBookId());

        Mockito.verify(bookRepository).save(book);
        Mockito.verify(bookTransactionRepository).save(Mockito.any(BookTransaction.class));
        Mockito.verify(notificationManager).notifyAll(user, "email.rent.confirmation", book.getTitle());
    }

    @Test
    void rentBook_shouldThrowExceptionWhenBookNotAvailable() {
        User user = getUser();
        Book book = getBook();

        book.setAvailable(false);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        assertThrows(IllegalStateException.class, () -> bookTransactionService.rentBook(user.getId(), book.getId()));
    }

    @Test
    void rentBook_shouldThrowExceptionWhenUserNotFound() {
        Long invalidUserId = 99L;

        Mockito.when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> bookTransactionService.rentBook(invalidUserId, 1L));
    }

    @Test
    void rentBook_shouldThrowExceptionWhenBookNotFound() {
        Long userId = 1L;
        Long invalidBookId = 99L;

        User user = getUser();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookTransactionService.rentBook(userId, invalidBookId));
    }

    @Test
    void shouldReturnBookSuccessfully() {
        User user = getUser();
        Book book = getBook();
        BookTransaction rental = getRental(user, book);

        LocalDate dateNow = LocalDate.now();

        BookTransaction updatedRental = getRental(user, book);
        updatedRental.setReturned(true);
        updatedRental.setReturnDate(dateNow);

        BookTransactionResponse response = getRentalResponse(updatedRental);

        Mockito.when(bookTransactionRepository.findByUserAndId(user, rental.getId())).thenReturn(Optional.of(rental));
        Mockito.when(bookTransactionRepository.save(Mockito.any(BookTransaction.class))).thenReturn(updatedRental);
        Mockito.when(rentalMapper.toRentalResponse(updatedRental)).thenReturn(response);

        BookTransactionResponse result = bookTransactionService.returnBook(user, rental.getId());

        assertNotNull(result);
        assertTrue(updatedRental.getReturned());
        assertEquals(dateNow, result.getReturnDate());
        assertEquals(1L, result.getId());

        Mockito.verify(bookRepository).save(book);
        Mockito.verify(bookTransactionRepository).save(Mockito.any(BookTransaction.class));
        Mockito.verify(notificationManager).notifyAll(user, "email.return.confirmation", book.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenBookAlreadyReturned() {
        Long rentalId = 1L;
        BookTransaction rental = BookTransaction.builder()
                .id(rentalId)
                .returned(true)
                .build();
        User user = getUser();

        Mockito.when(bookTransactionRepository.findByUserAndId(user, rentalId)).thenReturn(Optional.of(rental));

        assertThrows(IllegalStateException.class, () -> bookTransactionService.returnBook(user, rentalId));

        Mockito.verify(bookTransactionRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(notificationManager, Mockito.never()).notifyAll(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldReturnListOfRentalResponses() {
        BookTransaction rental1 = BookTransaction.builder().id(1L).build();
        BookTransaction rental2 = BookTransaction.builder().id(2L).build();

        List<BookTransaction> rentals = List.of(rental1, rental2);

        BookTransactionResponse response1 = BookTransactionResponse.builder().id(1L).build();
        BookTransactionResponse response2 = BookTransactionResponse.builder().id(2L).build();

        Mockito.when(bookTransactionRepository.findAll()).thenReturn(rentals);
        Mockito.when(rentalMapper.toRentalResponse(rental1)).thenReturn(response1);
        Mockito.when(rentalMapper.toRentalResponse(rental2)).thenReturn(response2);

        List<BookTransactionResponse> result = bookTransactionService.getAllRentals();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        Mockito.verify(bookTransactionRepository).findAll();
        Mockito.verify(rentalMapper).toRentalResponse(rental1);
        Mockito.verify(rentalMapper).toRentalResponse(rental2);
    }

    @ParameterizedTest
    @MethodSource("provideFiltersAndExpectedRentals")
    void shouldReturnUserRentalHistoryFiltered(RentalFilter filter, List<BookTransaction> rentalsFromRepo) {
        Long userId = 1L;
        User user = getUser();
        Book book = getBook();

        RentalHistoryResponse expectedResponse = getRentalHistoryResponse(user, book, rentalsFromRepo.getFirst());

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(rentalMapper.toHistoryResponse(Mockito.any())).thenReturn(expectedResponse);

        switch (filter) {
            case ACTIVE -> Mockito.when(bookTransactionRepository.findByUserAndReturned(user, false)).thenReturn(rentalsFromRepo);
            case RETURNED -> Mockito.when(bookTransactionRepository.findByUserAndReturned(user, true)).thenReturn(rentalsFromRepo);
            case ALL -> Mockito.when(bookTransactionRepository.findAllByUser(user)).thenReturn(rentalsFromRepo);
        }

        List<RentalHistoryResponse> result = bookTransactionService.getUserRentalHistory(userId, filter);

        assertNotNull(result);
        assertEquals(rentalsFromRepo.size(), result.size());
        assertEquals(expectedResponse.getRentalId(), result.getFirst().getRentalId());
    }

    @Test
    void getUserRentalHistory_shouldThrowExceptionWhenUserNotFound() {
        Long invalidUserId = 99L;
        Mockito.when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> bookTransactionService.getUserRentalHistory(invalidUserId, RentalFilter.ALL));
    }

    @ParameterizedTest
    @MethodSource("provideFiltersAndExpectedRentals")
    void shouldReturnBookRentalHistoryFiltered(RentalFilter filter, List<BookTransaction> rentalsFromRepo) {
        Long bookId = 1L;
        Book book = getBook();
        User user = getUser();

        RentalHistoryResponse expectedResponse = getRentalHistoryResponse(user, book, rentalsFromRepo.getFirst());

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(rentalMapper.toHistoryResponse(Mockito.any())).thenReturn(expectedResponse);

        switch (filter) {
            case ACTIVE -> Mockito.when(bookTransactionRepository.findByBookAndReturned(book, false)).thenReturn(rentalsFromRepo);
            case RETURNED -> Mockito.when(bookTransactionRepository.findByBookAndReturned(book, true)).thenReturn(rentalsFromRepo);
            case ALL -> Mockito.when(bookTransactionRepository.findAllByBook(book)).thenReturn(rentalsFromRepo);
        }

        List<RentalHistoryResponse> result = bookTransactionService.getBookRentalHistory(bookId, filter);

        assertNotNull(result);
        assertEquals(rentalsFromRepo.size(), result.size());
        assertEquals(expectedResponse.getRentalId(), result.getFirst().getRentalId());
    }

    @Test
    void getBookRentalHistory_shouldThrowExceptionWhenBookNotFound() {
        Long invalidBookId = 99L;
        Mockito.when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookTransactionService.getBookRentalHistory(invalidBookId, RentalFilter.ALL));
    }

    private static Stream<Arguments> provideFiltersAndExpectedRentals() {
        BookTransaction rental1 = BookTransaction.builder().id(1L).returned(false).build();
        BookTransaction rental2 = BookTransaction.builder().id(1L).returned(true).build();

        return Stream.of(
                Arguments.of(RentalFilter.ACTIVE, List.of(rental1)),
                Arguments.of(RentalFilter.RETURNED, List.of(rental2)),
                Arguments.of(RentalFilter.ALL, List.of(rental1, rental2))
        );
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .fullName("John")
                .build();
    }

    private Book getBook() {
        return Book.builder()
                .id(1L)
                .title("Clean Code")
                .available(true)
                .build();
    }

    private BookTransaction getRental(User user, Book book) {
        return BookTransaction.builder()
                .id(1L)
                .user(user)
                .book(book)
                .transactionDate(LocalDateTime.now())
                .dueDate(LocalDate.now().plusDays(14))
                .returnDate(null)
                .returned(false)
                .build();
    }

    private BookTransactionResponse getRentalResponse(BookTransaction rental) {
        return BookTransactionResponse.builder()
                .id(rental.getId())
                .userId(rental.getUser().getId())
                .bookId(rental.getBook().getId())
                .bookTitle(rental.getBook().getTitle())
                .returnDate(rental.getReturnDate())
                .returned(rental.getReturned())
                .build();
    }

    private RentalHistoryResponse getRentalHistoryResponse(User user, Book book, BookTransaction rental) {
        return RentalHistoryResponse.builder()
                .rentalId(rental.getId())
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .userId(user.getId())
                .build();
    }
}