package com.wallet.auth.validator;

import com.wallet.auth.model.User;
import com.wallet.auth.service.DetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomValidator implements Validator {
    private final DetailsService detailsService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            detailsService.loadUserByUsername(user.getLogin());
            errors.rejectValue("login", "", "Login already exists");
            log.warn("Login already exists: {}", user.getLogin());
        } catch (UsernameNotFoundException ignored) {
        }

    }

}