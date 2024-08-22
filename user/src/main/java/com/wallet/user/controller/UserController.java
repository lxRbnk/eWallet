package com.wallet.user.controller;

import com.wallet.user.dto.UserDto;
import com.wallet.user.model.User;
import com.wallet.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info/{id}") //fixme, get id from token
    public String getInfo(@PathVariable Long id){
        return userService.getUserById(id).getFirstName();
    }

    @GetMapping
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }

    @PostMapping
    public void create(@RequestBody UserDto userDto){
        userService.create(userDto);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody UserDto userDto,
                       @PathVariable Long id){
        userService.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }

}
