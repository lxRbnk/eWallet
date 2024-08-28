package com.wallet.auth.service;

import com.wallet.auth.model.User;
import com.wallet.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void create(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public Optional<User> findUser(String login) {
        return userRepository.findUserByLogin(login);
    }

    public void delete(String login) {
        Optional<User> userOptional = userRepository.findUserByLogin(login);
        if(userOptional.isPresent()){
            long id = userOptional.get().getId();
            userRepository.deleteById(id);
        }
    }
}
