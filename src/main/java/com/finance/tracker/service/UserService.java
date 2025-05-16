package com.finance.tracker.service;

import com.finance.tracker.entity.User;
import com.finance.tracker.model.UserDTO;
import com.finance.tracker.repository.UserRepository;
import com.finance.tracker.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userEntity = userRepository.save(user);
        return MapperUtil.toUserDTO(userEntity);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(MapperUtil::toUserDTO)
                .collect(Collectors.toList());
    }
}
