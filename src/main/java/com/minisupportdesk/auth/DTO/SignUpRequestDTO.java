package com.minisupportdesk.auth.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDTO {

    @NotBlank(message = "Full Name must be required")
    private String fullName;

    @Email(message = "Invalid input Email ID")
    @NotBlank(message = "Username must be required")
    private String username;

    @NotBlank(message = "Password must be required")
    private String password;

}
