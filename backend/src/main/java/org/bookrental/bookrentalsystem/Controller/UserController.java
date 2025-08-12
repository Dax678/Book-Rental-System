package org.bookrental.bookrentalsystem.Controller;

import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.bookrental.bookrentalsystem.Data.Response.UserResponse;
import org.bookrental.bookrentalsystem.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getLoggedUser(@AuthenticationPrincipal User currentUser) {
        UserResponse userResponse = userService.getLoggedUser(currentUser);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<UserResponse>> getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.getUsersByName(name));
    }
}
