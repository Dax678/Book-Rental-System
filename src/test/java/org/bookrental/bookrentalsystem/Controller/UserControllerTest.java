package org.bookrental.bookrentalsystem.Controller;

import org.bookrental.bookrentalsystem.Data.Response.UserResponse;
import org.bookrental.bookrentalsystem.Service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "jan@example.com", roles = {"LIBRARIAN"})
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
    @WithMockUser(username = "jan@example.com", roles = {"STUDENT"})
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
    @WithMockUser(username = "jan@example.com", roles = {"LIBRARIAN"})
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

    private UserResponse getUserResponse(Long id, String name) {
        return UserResponse.builder()
                .id(id)
                .name(name)
                .build();
    }
}