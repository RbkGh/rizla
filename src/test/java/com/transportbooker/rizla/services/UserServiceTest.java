package com.transportbooker.rizla.services;

import com.transportbooker.rizla.models.CustomUser;
import com.transportbooker.rizla.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loadUserByUsername_userExists_returnsUserDetails() {

        CustomUser user = new CustomUser();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(Set.of("ROLE_USER"));

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails userDetails = authService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_userNotFound_throwsException() {
        Mockito.when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername("nonexistent"));
    }

    @Test
    void saveUser_encodesPasswordAndSavesUser() {
        CustomUser user = new CustomUser();
        user.setUsername("newuser");
        user.setPassword("password");

        Mockito.when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(ArgumentMatchers.any(CustomUser.class))).thenReturn(user);

        CustomUser savedUser = userService.saveUser(user); //save user with password hashed

        assertEquals("encodedPassword", savedUser.getPassword());
        Mockito.verify(userRepository).save(user);
    }
}