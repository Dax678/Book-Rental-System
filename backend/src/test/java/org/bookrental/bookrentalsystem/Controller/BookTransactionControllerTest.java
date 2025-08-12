package org.bookrental.bookrentalsystem.Controller;

import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.bookrental.bookrentalsystem.Data.Response.RentalHistoryResponse;
import org.bookrental.bookrentalsystem.Data.Response.BookTransactionResponse;
import org.bookrental.bookrentalsystem.Service.BookTransactionService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookTransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookTransactionService bookTransactionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "jan@example.com", roles = {"STUDENT"})
    void shouldRentBook() throws Exception {
        Long userId = 1L;
        Long bookId = 1L;

        BookTransactionResponse response = getRentalResponse();

        Mockito.when(bookTransactionService.rentBook(userId, bookId)).thenReturn(response);

        mockMvc.perform(post("/api/rentals/rent")
                        .param("userId", String.valueOf(userId))
                        .param("bookId", String.valueOf(bookId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.bookTitle").value("Clean Code"));
    }

    @Test
    @WithMockUser(username = "jan@example.com", roles = {"STUDENT"})
    void shouldReturnBook() throws Exception {
        Long rentalId = 1L;
        Long userId = 1L;
        BookTransactionResponse response = getRentalResponse();

        User user = User.builder()
                .id(userId)
                .build();

        Mockito.when(bookTransactionService.returnBook(user, rentalId)).thenReturn(response);

        mockMvc.perform(post("/api/rentals/return/{rentalId}", rentalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returned").value(true));
    }

    @Test
    @WithMockUser(username = "jan@example.com", roles = {"LIBRARIAN"})
    void shouldGetAllRentals() throws Exception {
        List<BookTransactionResponse> rentals = List.of(getRentalResponse());

        Mockito.when(bookTransactionService.getAllRentals()).thenReturn(rentals);

        mockMvc.perform(get("/api/rentals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser(username = "jan@example.com", roles = {"STUDENT"})
    void shouldGetUserRentalHistory() throws Exception {
        Long userId = 1L;
        List<RentalHistoryResponse> history = List.of(getRentalHistoryResponse());

        Mockito.when(bookTransactionService.getUserRentalHistory(userId, RentalFilter.ALL)).thenReturn(history);

        mockMvc.perform(get("/api/rentals/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser(username = "jan@example.com", roles = {"LIBRARIAN"})
    void shouldGetBookRentalHistory() throws Exception {
        Long bookId = 1L;
        List<RentalHistoryResponse> history = List.of(getRentalHistoryResponse());

        Mockito.when(bookTransactionService.getBookRentalHistory(bookId, RentalFilter.RETURNED)).thenReturn(history);

        mockMvc.perform(get("/api/rentals/book/{bookId}?filter=RETURNED", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    private BookTransactionResponse getRentalResponse() {
        return BookTransactionResponse.builder()
                .id(1L)
                .bookTitle("Clean Code")
                .returned(true)
                .build();
    }

    private RentalHistoryResponse getRentalHistoryResponse() {
        return RentalHistoryResponse.builder()
                .rentalId(1L)
                .bookTitle("Clean Code")
                .build();
    }
}