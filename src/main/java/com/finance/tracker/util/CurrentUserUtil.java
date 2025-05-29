package com.finance.tracker.util;

import com.finance.tracker.entity.User;
import com.finance.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserUtil {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found.");
        }

        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername(); // usually email
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new RuntimeException("Invalid user principal");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found for username: " + username));
    }
}
