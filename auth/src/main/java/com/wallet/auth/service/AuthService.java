package com.wallet.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.auth.dto.AuthenticationDto;
import com.wallet.auth.dto.UserDto;
import com.wallet.auth.model.User;
import com.wallet.auth.util.JwtUtil;
import com.wallet.auth.validator.CustomValidator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CustomValidator customValidator;
    private final AuthenticationManager authenticationManager;
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;

    public ResponseEntity<Map<String, String>> registrationUser(UserDto userDto, BindingResult bindingResult) {
        User userAuth = convertToUserAuth(userDto);
        customValidator.validate(userAuth, bindingResult);
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            log.error("Registration failed " + errors);
            return ResponseEntity.badRequest()
                    .body(Map.of("Message ", "Registration failed " + errors));
        }
        userDto.setPassword(null);
        try {
            String userJson = objectMapper.writeValueAsString(userDto);
            kafkaProducerService.sendUserCreationMessage(userJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("Failed to serialize UserDto: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("Message",
                    "Registration failed " + e.getMessage()));
        }
        userService.create(userAuth);
        String token = jwtUtil.generateToken(userAuth.getLogin());
        log.info("Registration is completed " + userAuth.getLogin());
        return ResponseEntity.ok().body(Map.of("Jwt token", token));
    }

    public ResponseEntity<Map<String, String>> loginUser(AuthenticationDto authenticationDto,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            log.warn("Registration failed " + errors);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("Message ", "Registration failed " + errors));
        }
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDto.getLogin(),
                        authenticationDto.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            log.warn("Login is failed " + authenticationDto.getLogin());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("Message ", "Incorrect login or password "));
        }
        String token = jwtUtil.generateToken(authenticationDto.getLogin());
        log.info("Login is completed " + authenticationDto.getLogin());
        return ResponseEntity.ok().body(Map.of("Jwt token", token));
    }

    private User convertToUserAuth(UserDto userDto) {
        return User.builder()
                .login(userDto.getLogin())
                .password(userDto.getPassword())
                .build();
    }

}
