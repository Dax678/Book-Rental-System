package org.bookrental.bookrentalsystem.Service;

import org.bookrental.bookrentalsystem.Config.Exception.UserNotFoundException;
import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.bookrental.bookrentalsystem.Data.Mapper.UserMapper;
import org.bookrental.bookrentalsystem.Data.Request.UserRequest;
import org.bookrental.bookrentalsystem.Data.Response.UserResponse;
import org.bookrental.bookrentalsystem.Notification.NotificationManager;
import org.bookrental.bookrentalsystem.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final NotificationManager notificationManager;

    public UserService(UserRepository userRepository, UserMapper userMapper, NotificationManager notificationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.notificationManager = notificationManager;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        validateUserListIsNotEmpty(users);

        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserById(Long userId) {
        User user = getUserByIdOrThrow(userId);

        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getUsersByName(String name) {
        List<User> users = userRepository.findAllByNameContainingIgnoreCase(name);

        validateUserListIsNotEmpty(users);

        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse createUser(UserRequest request) {
        User user = userMapper.toEntity(request);
        User saved = userRepository.save(user);

        notificationManager.notifyAll(saved,
                "email.welcome",
                saved.getName());

        return userMapper.toUserResponse(saved);
    }

    private User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private void validateUserListIsNotEmpty(List<User> users) {
        if(users.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
    }
}
