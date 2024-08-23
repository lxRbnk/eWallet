package com.wallet.user.controller;

import com.wallet.user.dto.UserDto;
import com.wallet.user.model.User;
import com.wallet.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/test")  //fixme
    public String test() {
        return "Test message";
    }

    @GetMapping("/info")
    public UserDto getInfo(@RequestHeader("Authorization") String token) {
        return userService.getUserByLogin(token);
    }

    @GetMapping("/{id}") //admin
    public UserDto getUser(@PathVariable long id) {
        UserDto userDto = userService.getUserById(id);
        return userDto;
    }

    @GetMapping("/all-info") //admin
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PostMapping //admin, without password
    public void create(@RequestBody UserDto userDto) {
        userService.create(userDto);
    }

    @PutMapping
    public void update(@RequestHeader("Authorization") String token,
                       @RequestBody UserDto userDto) {
        userService.update(userDto, token);
    }

    @DeleteMapping("/{id}") //admin
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @DeleteMapping
    public void delete(@RequestHeader("Authorization") String token) {
        userService.deleteCurrentUser(token);
    }

}
