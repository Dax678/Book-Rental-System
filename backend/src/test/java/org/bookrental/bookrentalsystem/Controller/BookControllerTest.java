package org.bookrental.bookrentalsystem.Controller;

import org.bookrental.bookrentalsystem.Data.Request.BookRequest;
import org.bookrental.bookrentalsystem.Data.Response.BookDetailsResponse;
import org.bookrental.bookrentalsystem.Service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "jan@example.com", roles = {"LIBRARIAN"})
    void shouldReturnBookById() throws Exception {
        Long bookId = 1L;
        BookDetailsResponse response = getBookResponse();

        Mockito.when(bookService.getBookDetailsById(bookId)).thenReturn(response);

        mockMvc.perform(get("/api/books/{bookId}", bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"))
                .andExpect(jsonPath("$.isbn").value("1234567890"));
    }

    @Test
    @WithMockUser(username = "jan@example.com", roles = {"LIBRARIAN"})
    void shouldCreateBookSuccessfully() throws Exception {
        BookRequest request = getBookRequest();

        BookDetailsResponse response = getBookResponse();

        Mockito.when(bookService.createBook(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/books/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"))
                .andExpect(jsonPath("$.isbn").value("1234567890"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBookRequests")
    @WithMockUser(username = "jan@example.com", roles = {"LIBRARIAN"})
    void shouldReturnBadRequestWhenBookRequestInvalid(BookRequest request) throws Exception {
        mockMvc.perform(post("/api/books/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private static Stream<BookRequest> provideInvalidBookRequests() {
        return Stream.of(
                BookRequest.builder().title("").author("Author").isbn("1234567890").build(),                    // empty title
                BookRequest.builder().title("Title").author("").isbn("1234567890").build(),                     // empty author
                BookRequest.builder().title("Title").author("Author").isbn("").build(),                         // empty ISBN
                BookRequest.builder().title("").author("").isbn("1234567890").build(),                          // empty title & author
                BookRequest.builder().title("Title").author("").isbn("").build(),                               // empty author & ISBN
                BookRequest.builder().title("").author("Author").isbn("").build(),                              // empty title & ISBN
                BookRequest.builder().title("").author("").isbn("").build(),                                    // all empty
                BookRequest.builder().title("Title").author("Author").isbn("123456789").build(),                // isbn < 10
                BookRequest.builder().title("Title").author("Author").isbn("123456789012345678901").build()     // isbn > 20
        );
    }

    private BookDetailsResponse getBookResponse() {
        return BookDetailsResponse.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("1234567890")
                .build();
    }

    private BookRequest getBookRequest() {
        return BookRequest.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("1234567890")
                .genre("IT")
                .build();
    }
}