package com.transportbooker.rizla.controllers;

import com.transportbooker.rizla.dto.request.UserRequestDTO;
import com.transportbooker.rizla.dto.response.UserResponseDTO;
import com.transportbooker.rizla.services.AuthService;
import com.transportbooker.rizla.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO user) {
        if (authService.loadUserByUsername(user.getUsername()) != null)
            return ResponseEntity.badRequest().body("Username already exists");

        UserResponseDTO userResponseDTO = userService.saveUser(user.toCustomUser()).toUserResponseDTO();
        return ResponseEntity.ok(userResponseDTO);
    }
}
