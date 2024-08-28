package com.wallet.user.dto;

import lombok.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class UserDto {

    private String login;

    @NotEmpty(message = "first name cannot be empty")
    private String firstName;

    @NotEmpty(message = "last name cannot be empty")
    private String lastName;

    @NotEmpty(message = "email cannot be empty")
    @Email
    private String email;

    @NotEmpty(message = "phone cannot be empty")
    private String phone;

    private LocalDateTime createdAt;
}
