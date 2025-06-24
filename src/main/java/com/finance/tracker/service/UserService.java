package com.finance.tracker.service;

import com.finance.tracker.entity.User;
import com.finance.tracker.mapper.UserMapper;
import com.finance.tracker.dto.UserDTO;
import com.finance.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserDTO registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userEntity = userRepository.save(user);
        return userMapper.toDTO(userEntity);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toDTO(user);
    }
}
