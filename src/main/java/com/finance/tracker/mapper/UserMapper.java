package com.finance.tracker.mapper;

import com.finance.tracker.entity.User;
import com.finance.tracker.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId()); // optional: may be null for creation
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        return user;
    }
}

