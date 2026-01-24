package com.minisupportdesk.auth.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminCreateDTO {

    @NotBlank(message = "Full Name must be required")
    String fullName;

    @Email(message = "Invalid input Email ID")
    @NotBlank(message = "Username must be required")
    String username;

    @NotBlank(message = "Password must be required")
    String password;

}
