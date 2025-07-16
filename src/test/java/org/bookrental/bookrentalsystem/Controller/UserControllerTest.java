package org.bookrental.bookrentalsystem.Controller;

import org.bookrental.bookrentalsystem.Data.Request.UserRequest;
import org.bookrental.bookrentalsystem.Data.Response.UserResponse;
import org.bookrental.bookrentalsystem.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<UserResponse> users = List.of(
                getUserResponse(1L, "Alice"),
                getUserResponse(2L, "Bob")
        );

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        Long userId = 1L;
        UserResponse user = getUserResponse(1L, "Alice");

        Mockito.when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void shouldReturnUsersByName() throws Exception {
        String userName = "Zofia";
        List<UserResponse> users = List.of(
                getUserResponse(1L, "Zofia")
        );

        Mockito.when(userService.getUsersByName(userName)).thenReturn(users);

        mockMvc.perform(get("/api/users/name/{userName}", userName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(userName));
    }

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        UserRequest request = UserRequest.builder()
                .name("John")
                .email("john@example.com")
                .userType("guest")
                .build();

        UserResponse response = UserResponse.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .userType("guest")
                .build();

        Mockito.when(userService.createUser(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.userType").value("guest"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserRequests")
    void shouldReturnBadRequestWhenInvalidUser(String name, String email) throws Exception {
        UserRequest request = UserRequest.builder()
                .name(name)
                .email(email)
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidUserRequests() {
        return Stream.of(
                Arguments.of("", "valid@example.com"),
                Arguments.of("John", ""),
                Arguments.of("", ""),
                Arguments.of(" ", "valid@example.com"),
                Arguments.of("John", "not-an-email"),
                Arguments.of("John", null),
                Arguments.of(null, "valid@example.com"),
                Arguments.of(null, null)
        );
    }

    private UserRequest getUserRequest(String name) {
        return UserRequest.builder()
                .name(name)
                .build();
    }

    private UserResponse getUserResponse(Long id, String name) {
        return UserResponse.builder()
                .id(id)
                .name(name)
                .build();
    }
}