package com.wallet.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@ToString
public class UserDto {
    @NotEmpty(message = "Login cannot be empty")
    @Size(min = 2, max = 100, message = "Login must be between 4 and 100 characters")
    private String login;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 4, max = 100, message = "Password must be between 4 and 100 characters")
    private String password;

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    @Email
    private String email;

    @NotEmpty(message = "Phone cannot be empty")
    private String phone;
}
