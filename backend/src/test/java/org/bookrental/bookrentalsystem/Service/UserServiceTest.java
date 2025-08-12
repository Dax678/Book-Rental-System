package org.bookrental.bookrentalsystem.Service;

import org.bookrental.bookrentalsystem.Config.Exception.UserNotFoundException;
import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.bookrental.bookrentalsystem.Data.Mapper.UserMapper;
import org.bookrental.bookrentalsystem.Data.Request.UserRequest;
import org.bookrental.bookrentalsystem.Data.Response.UserResponse;
import org.bookrental.bookrentalsystem.Notification.NotificationManager;
import org.bookrental.bookrentalsystem.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private NotificationManager notificationManager;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnAllUsers() {
        User user = getUser();
        UserResponse userResponse = getUserResponse();

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.getFirst().getId());
    }

    @Test
    void shouldThrowExceptionWhenNoUsersFound() {
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(UserNotFoundException.class, () -> userService.getAllUsers());
    }

    @Test
    void shouldReturnUserById() {
        User user = getUser();
        UserResponse userResponse = getUserResponse();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    void shouldThrowExceptionWhenUserByIdNotFound() {
        Mockito.when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void shouldReturnUsersByName() {
        User user = getUser();
        UserResponse response = getUserResponse();

        Mockito.when(userRepository.findAllByFullNameContainingIgnoreCase("john")).thenReturn(List.of(user));
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(response);

        List<UserResponse> result = userService.getUsersByName("john");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getName());
    }

    @Test
    void shouldThrowExceptionWhenUsersByNameNotFound() {
        Mockito.when(userRepository.findAllByFullNameContainingIgnoreCase("unknown")).thenReturn(Collections.emptyList());

        assertThrows(UserNotFoundException.class, () -> userService.getUsersByName("unknown"));
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .fullName("John")
                .email("john@example.com")
                .build();
    }

    private UserResponse getUserResponse() {
        return UserResponse.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .build();
    }

    private UserRequest getUserRequest() {
        return UserRequest.builder()
                .name("John")
                .email("john@example.com")
                .build();
    }
}