package org.bookrental.bookrentalsystem.Controller;

import org.bookrental.bookrentalsystem.Data.Request.AuthRequest;
import org.bookrental.bookrentalsystem.Data.Request.RegisterRequest;
import org.bookrental.bookrentalsystem.Data.Response.AuthResponse;
import org.bookrental.bookrentalsystem.Data.Response.RegisterResponse;
import org.bookrental.bookrentalsystem.Service.AuthService;
import org.bookrental.bookrentalsystem.Service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldAuthenticateUserSuccessfully() throws Exception {
        AuthRequest request = new AuthRequest("jan@example.com", "password");
        AuthResponse response = new AuthResponse("mock-jwt-token", 360000);

        Mockito.when(authService.authenticate(Mockito.any(AuthRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidAuthRequests")
    void shouldReturnBadRequestWhenAuthRequestInvalid(AuthRequest invalidRequest) throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("Jan Kowalski")
                .email("jan@example.com")
                .password("password123")
                .build();

        RegisterResponse response = RegisterResponse.builder()
                .email("jan@example.com")
                .message("User registered successfully")
                .build();

        Mockito.when(authService.signup(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("jan@example.com"))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRegisterRequests")
    void shouldReturnBadRequestWhenRegisterRequestInvalid(RegisterRequest invalidRequest) throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private static Stream<AuthRequest> provideInvalidAuthRequests() {
        return Stream.of(
                new AuthRequest("", "password"),                // empty email
                new AuthRequest("invalid-email", "password"),   // invalid email
                new AuthRequest("jan@example.com", ""),         // empty password
                new AuthRequest("", ""),                        // all fields empty
                new AuthRequest(null, "password"),              // null email
                new AuthRequest("jan@example.com", null)        // null password
        );
    }

    private static Stream<RegisterRequest> provideInvalidRegisterRequests() {
        return Stream.of(
                RegisterRequest.builder().fullName("").email("jan@example.com").password("password123").build(),    // empty name
                RegisterRequest.builder().fullName("Jan").email("").password("password123").build(),                // empty email
                RegisterRequest.builder().fullName("Jan").email("invalid-email").password("password123").build(),   // invalid email
                RegisterRequest.builder().fullName("Jan").email("jan@example.com").password("").build(),            // empty password
                RegisterRequest.builder().fullName("").email("").password("").build(),                              // all fields empty
                RegisterRequest.builder().fullName(null).email("jan@example.com").password("password123").build(),  // null name
                RegisterRequest.builder().fullName("Jan").email(null).password("password123").build(),              // null email
                RegisterRequest.builder().fullName("Jan").email("jan@example.com").password(null).build()           // null password
        );
    }
}