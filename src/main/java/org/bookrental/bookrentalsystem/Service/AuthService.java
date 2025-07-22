package org.bookrental.bookrentalsystem.Service;

import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.bookrental.bookrentalsystem.Data.Entity.UserType;
import org.bookrental.bookrentalsystem.Data.Request.AuthRequest;
import org.bookrental.bookrentalsystem.Data.Request.RegisterRequest;
import org.bookrental.bookrentalsystem.Data.Response.AuthResponse;
import org.bookrental.bookrentalsystem.Data.Response.RegisterResponse;
import org.bookrental.bookrentalsystem.Notification.NotificationManager;
import org.bookrental.bookrentalsystem.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final NotificationManager notificationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, NotificationManager notificationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.notificationManager = notificationManager;
    }

    public RegisterResponse signup(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .userType(UserType.CUSTOMER)
                .build();

        User saved = userRepository.save(user);

        notificationManager.notifyAll(saved,
                "email.welcome",
                saved.getFullName());

        return RegisterResponse.builder()
                .message("User registered successfully")
                .email(saved.getEmail())
                .build();
    }

    public AuthResponse authenticate(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = getUser(request);

        String jwtToken = jwtService.generateToken(user);
        long expirationTime = jwtService.getExpirationTime();

        return new AuthResponse(jwtToken, expirationTime);
    }

    private User getUser(AuthRequest request) {
        return userRepository.findByEmail(request.getUsername())
                .orElseThrow();
    }
}
