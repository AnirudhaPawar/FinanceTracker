package com.finance.tracker.service;

import com.finance.tracker.dto.UserDTO;
import com.finance.tracker.entity.User;
import com.finance.tracker.mapper.UserMapper;
import com.finance.tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setPassword("plainPassword");

        User encodedUser = new User();
        encodedUser.setPassword("encodedPassword");

        User savedUser = new User();
        UserDTO userDTO = new UserDTO();

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDTO(savedUser)).thenReturn(userDTO);

        UserDTO result = userService.registerUser(user);

        assertNotNull(result);
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(user);
        verify(userMapper).toDTO(savedUser);
    }

    @Test
    void testGetAllUsers() {
        User user = new User();
        UserDTO dto = new UserDTO();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testUpdateUser_Success() {
        Long userId = 1L;
        User inputUser = new User();
        inputUser.setUsername("newUsername");
        inputUser.setPassword("newPassword");

        User existingUser = new User();
        existingUser.setId(userId);

        User savedUser = new User();
        UserDTO dto = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(userRepository.save(existingUser)).thenReturn(savedUser);
        when(userMapper.toDTO(savedUser)).thenReturn(dto);

        UserDTO result = userService.updateUser(userId, inputUser);

        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(existingUser);
        verify(userMapper).toDTO(savedUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                userService.updateUser(1L, new User()));
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void testGetUserById_Success() {
        Long userId = 1L;
        User user = new User();
        UserDTO dto = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        UserDTO result = userService.getUserById(userId);

        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userMapper).toDTO(user);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(1L));
    }
}
