package org.bookrental.bookrentalsystem.Service;

import org.bookrental.bookrentalsystem.Config.Exception.BookNotFoundException;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RentalMapper rentalMapper;

    @Mock
    private NotificationManager notificationManager;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void shouldRentBookSuccessfully() {
        User user = getUser();

        Book book = getBook();

        Rental rental = getRental(user, book);

        RentalResponse response = getRentalResponse(rental);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        Mockito.when(rentalRepository.save(Mockito.any(Rental.class))).thenReturn(rental);
        Mockito.when(rentalMapper.toRentalResponse(rental)).thenReturn(response);

        RentalResponse result = rentalService.rentBook(user.getId(), book.getId());

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getBookId());

        Mockito.verify(bookRepository).save(book);
        Mockito.verify(rentalRepository).save(Mockito.any(Rental.class));
        Mockito.verify(notificationManager).notifyAll(user, "email.rent.confirmation", book.getTitle());
    }

    @Test
    void rentBook_shouldThrowExceptionWhenBookNotAvailable() {
        User user = getUser();
        Book book = getBook();

        book.setAvailable(false);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        assertThrows(IllegalStateException.class, () -> rentalService.rentBook(user.getId(), book.getId()));
    }

    @Test
    void rentBook_shouldThrowExceptionWhenUserNotFound() {
        Long invalidUserId = 99L;

        Mockito.when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> rentalService.rentBook(invalidUserId, 1L));
    }

    @Test
    void rentBook_shouldThrowExceptionWhenBookNotFound() {
        Long userId = 1L;
        Long invalidBookId = 99L;

        User user = getUser();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> rentalService.rentBook(userId, invalidBookId));
    }

    @Test
    void shouldReturnBookSuccessfully() {
        User user = getUser();
        Book book = getBook();
        Rental rental = getRental(user, book);

        LocalDate dateNow = LocalDate.now();

        Rental updatedRental = getRental(user, book);
        updatedRental.setReturned(true);
        updatedRental.setReturnDate(dateNow);

        RentalResponse response = getRentalResponse(updatedRental);

        Mockito.when(rentalRepository.findById(rental.getId())).thenReturn(Optional.of(rental));
        Mockito.when(rentalRepository.save(Mockito.any(Rental.class))).thenReturn(updatedRental);
        Mockito.when(rentalMapper.toRentalResponse(updatedRental)).thenReturn(response);

        RentalResponse result = rentalService.returnBook(rental.getId());

        assertNotNull(result);
        assertTrue(updatedRental.isReturned());
        assertEquals(dateNow, result.getReturnDate());
        assertEquals(1L, result.getId());

        Mockito.verify(bookRepository).save(book);
        Mockito.verify(rentalRepository).save(Mockito.any(Rental.class));
        Mockito.verify(notificationManager).notifyAll(user, "email.return.confirmation", book.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenBookAlreadyReturned() {
        Long rentalId = 1L;
        Rental rental = Rental.builder()
                .id(rentalId)
                .returned(true)
                .build();

        Mockito.when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        assertThrows(IllegalStateException.class, () -> rentalService.returnBook(rentalId));

        Mockito.verify(rentalRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(notificationManager, Mockito.never()).notifyAll(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldReturnListOfRentalResponses() {
        Rental rental1 = Rental.builder().id(1L).build();
        Rental rental2 = Rental.builder().id(2L).build();

        List<Rental> rentals = List.of(rental1, rental2);

        RentalResponse response1 = RentalResponse.builder().id(1L).build();
        RentalResponse response2 = RentalResponse.builder().id(2L).build();

        Mockito.when(rentalRepository.findAll()).thenReturn(rentals);
        Mockito.when(rentalMapper.toRentalResponse(rental1)).thenReturn(response1);
        Mockito.when(rentalMapper.toRentalResponse(rental2)).thenReturn(response2);

        List<RentalResponse> result = rentalService.getAllRentals();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        Mockito.verify(rentalRepository).findAll();
        Mockito.verify(rentalMapper).toRentalResponse(rental1);
        Mockito.verify(rentalMapper).toRentalResponse(rental2);
    }

    @ParameterizedTest
    @MethodSource("provideFiltersAndExpectedRentals")
    void shouldReturnUserRentalHistoryFiltered(RentalFilter filter, List<Rental> rentalsFromRepo) {
        Long userId = 1L;
        User user = getUser();
        Book book = getBook();

        RentalHistoryResponse expectedResponse = getRentalHistoryResponse(user, book, rentalsFromRepo.getFirst());

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(rentalMapper.toHistoryResponse(Mockito.any())).thenReturn(expectedResponse);

        switch (filter) {
            case ACTIVE -> Mockito.when(rentalRepository.findByUserAndReturned(user, false)).thenReturn(rentalsFromRepo);
            case RETURNED -> Mockito.when(rentalRepository.findByUserAndReturned(user, true)).thenReturn(rentalsFromRepo);
            case ALL -> Mockito.when(rentalRepository.findAllByUser(user)).thenReturn(rentalsFromRepo);
        }

        List<RentalHistoryResponse> result = rentalService.getUserRentalHistory(userId, filter);

        assertNotNull(result);
        assertEquals(rentalsFromRepo.size(), result.size());
        assertEquals(expectedResponse.getRentalId(), result.getFirst().getRentalId());
    }

    @Test
    void getUserRentalHistory_shouldThrowExceptionWhenUserNotFound() {
        Long invalidUserId = 99L;
        Mockito.when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> rentalService.getUserRentalHistory(invalidUserId, RentalFilter.ALL));
    }

    @ParameterizedTest
    @MethodSource("provideFiltersAndExpectedRentals")
    void shouldReturnBookRentalHistoryFiltered(RentalFilter filter, List<Rental> rentalsFromRepo) {
        Long bookId = 1L;
        Book book = getBook();
        User user = getUser();

        RentalHistoryResponse expectedResponse = getRentalHistoryResponse(user, book, rentalsFromRepo.getFirst());

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(rentalMapper.toHistoryResponse(Mockito.any())).thenReturn(expectedResponse);

        switch (filter) {
            case ACTIVE -> Mockito.when(rentalRepository.findByBookAndReturned(book, false)).thenReturn(rentalsFromRepo);
            case RETURNED -> Mockito.when(rentalRepository.findByBookAndReturned(book, true)).thenReturn(rentalsFromRepo);
            case ALL -> Mockito.when(rentalRepository.findAllByBook(book)).thenReturn(rentalsFromRepo);
        }

        List<RentalHistoryResponse> result = rentalService.getBookRentalHistory(bookId, filter);

        assertNotNull(result);
        assertEquals(rentalsFromRepo.size(), result.size());
        assertEquals(expectedResponse.getRentalId(), result.getFirst().getRentalId());
    }

    @Test
    void getBookRentalHistory_shouldThrowExceptionWhenBookNotFound() {
        Long invalidBookId = 99L;
        Mockito.when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> rentalService.getBookRentalHistory(invalidBookId, RentalFilter.ALL));
    }

    private static Stream<Arguments> provideFiltersAndExpectedRentals() {
        Rental rental1 = Rental.builder().id(1L).returned(false).build();
        Rental rental2 = Rental.builder().id(1L).returned(true).build();

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

    private Rental getRental(User user, Book book) {
        return Rental.builder()
                .id(1L)
                .user(user)
                .book(book)
                .rentedAt(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .returnDate(null)
                .returned(false)
                .build();
    }

    private RentalResponse getRentalResponse(Rental rental) {
        return RentalResponse.builder()
                .id(rental.getId())
                .userId(rental.getUser().getId())
                .bookId(rental.getBook().getId())
                .bookTitle(rental.getBook().getTitle())
                .returnDate(rental.getReturnDate())
                .returned(rental.isReturned())
                .build();
    }

    private RentalHistoryResponse getRentalHistoryResponse(User user, Book book, Rental rental) {
        return RentalHistoryResponse.builder()
                .rentalId(rental.getId())
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .userId(user.getId())
                .build();
    }
}