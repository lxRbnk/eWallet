package com.wallet.auth.controller;

import com.wallet.auth.dto.AuthenticationDto;
import com.wallet.auth.dto.UserDto;
import com.wallet.auth.service.AuthService;
import com.wallet.auth.service.DetailsService;
import com.wallet.auth.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/test")
    public String go(){
        return "test message";
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> performRegistration(@RequestBody @Valid UserDto userDto,
                                                                  BindingResult bindingResult) {
        return authService.registrationUser(userDto, bindingResult);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody @Valid AuthenticationDto authenticationDto,
                                            BindingResult bindingResult) {
        return authService.loginUser(authenticationDto, bindingResult);
    }
}


