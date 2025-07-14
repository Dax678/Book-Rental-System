package org.bookrental.bookrentalsystem.Data.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest {
    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String userType;
}
