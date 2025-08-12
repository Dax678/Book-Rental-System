package org.bookrental.bookrentalsystem.Data.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
