package com.wallet.auth.service;

import com.wallet.auth.model.User;
import com.wallet.auth.repository.UserRepository;
import com.wallet.auth.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = repository.findUserByLogin(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(user.get());
    }
}
