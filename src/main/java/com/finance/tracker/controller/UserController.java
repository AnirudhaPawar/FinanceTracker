package com.finance.tracker.controller;

import com.finance.tracker.entity.User;
import com.finance.tracker.dto.UserDTO;
import com.finance.tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserDTO createUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserDTO editUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

}
