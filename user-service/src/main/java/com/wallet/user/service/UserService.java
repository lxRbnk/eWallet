package com.wallet.user.service;

import com.wallet.user.dto.UserDto;
import com.wallet.user.kafka.DeleteUserProducer;
import com.wallet.user.model.User;
import com.wallet.user.repository.UserRepository;
import com.wallet.user.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final DeleteUserProducer producerService;

    public UserDto getUserByLogin(String token) {
        String login = jwtUtil.validateTokenAndGetClaim(token);
        User user = userRepository.findUserByLogin(login);
        if (user != null) {
            return entityToDto(user);
        } else {
            return null;
        }
    }

    public UserDto getUserById(long id) {
        User user = userRepository.getUserById(id);
        if (user != null) {
            return entityToDto(user);
        } else {
            return null;
        }
    }

    public List<UserDto> getUsers() {
        List<User> userList = userRepository.findAll();
        if (userList != null) {
            return userList.stream()
                    .map(this::entityToDto)
                    .toList();
        } else {
            return null;
        }
    }

    public void create(UserDto userDto) {
        User user = dtoToEntity(userDto);
        userRepository.save(user);
    }

    public boolean update(UserDto userDto, String token) {
        try {
            String login = jwtUtil.validateTokenAndGetClaim(token);
            User currentUser = userRepository.findUserByLogin(login);
            LocalDateTime createdAt = currentUser.getCreatedAt();
            long id = currentUser.getId();
            User user = dtoToEntity(userDto);
            user.setId(id);
            user.setCreatedAt(createdAt);
            user.setLogin(login);
            userRepository.save(user);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean delete(Long id) {
        try {
            String login = userRepository.getUserById(id).getLogin();
            producerService.sendUserDeleteMessage(login);
            userRepository.deleteById(id);
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

    public boolean deleteCurrentUser(String token) {
        try {
            String login = jwtUtil.validateTokenAndGetClaim(token);
            long id = userRepository.findUserByLogin(login).getId();
            producerService.sendUserDeleteMessage(login);
            userRepository.deleteById(id);
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

    private User dtoToEntity(UserDto userDto) {
        return User.builder()
                .login(userDto.getLogin())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phone(userDto.getPhone())
                .email(userDto.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private UserDto entityToDto(User user) {
        return UserDto.builder()
                .login(user.getLogin())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
