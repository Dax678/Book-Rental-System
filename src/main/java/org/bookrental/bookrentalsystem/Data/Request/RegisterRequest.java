package org.bookrental.bookrentalsystem.Data.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    @NotBlank
    @Email
    private String email;

    @Size(min = 6)
    @NotBlank
    private String password;

    @NotBlank
    private String fullName;
}
