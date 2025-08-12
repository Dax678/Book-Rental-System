package org.bookrental.bookrentalsystem.Data.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private long expiresIn;
}
