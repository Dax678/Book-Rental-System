package org.bookrental.bookrentalsystem.Controller;

import jakarta.validation.Valid;
import org.bookrental.bookrentalsystem.Data.Request.AuthRequest;
import org.bookrental.bookrentalsystem.Data.Request.RegisterRequest;
import org.bookrental.bookrentalsystem.Data.Response.AuthResponse;
import org.bookrental.bookrentalsystem.Data.Response.RegisterResponse;
import org.bookrental.bookrentalsystem.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.authenticate(authRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
