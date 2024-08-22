package com.wallet.user.service;

import com.wallet.user.dto.UserDto;
import com.wallet.user.model.User;
import com.wallet.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id){
        return userRepository.getUserById(id);
    }

    public List<UserDto> getUsers(){
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(this::entityToDto)
                .toList();
    }

    public void create(UserDto userDto){
        User user = dtoToEntity(userDto);
        userRepository.save(user);
    }

    public void update(UserDto userDto, Long id){
        LocalDateTime createdAt = userRepository.getUserById(id).getCreatedAt();
        User user = dtoToEntity(userDto);
        user.setId(id);
        user.setCreatedAt(createdAt);
        userRepository.save(user);
    }

    public void delete(Long id){
        userRepository.deleteById(id);
    }

    private User dtoToEntity(UserDto userDto){
        return User.builder()
                .login(userDto.getLogin())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phone(userDto.getPhone())
                .email(userDto.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private UserDto entityToDto(User user){
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
