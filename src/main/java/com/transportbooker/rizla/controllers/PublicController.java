package com.transportbooker.rizla.controllers;

import com.transportbooker.rizla.dto.request.LoginRequestDTO;
import com.transportbooker.rizla.dto.request.UserRequestDTO;
import com.transportbooker.rizla.dto.response.LoginResponseDTO;
import com.transportbooker.rizla.dto.response.UserResponseDTO;
import com.transportbooker.rizla.services.AuthService;
import com.transportbooker.rizla.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/public")
public class PublicController {

    private UserService userService;
    private AuthService authService;

    public PublicController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequestDTO authenticationRequest) throws Exception {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        String jwtToken = authService.createAuthenticationToken(username, password);

        return ResponseEntity.ok(LoginResponseDTO.builder().jwtToken(jwtToken).build());
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO user) {
        if (userService.findUserByUserName(user.getUsername()).isPresent())
            return ResponseEntity.badRequest().body("Username already exists");

        UserResponseDTO userResponseDTO = userService.saveUser(user.toCustomUser()).toUserResponseDTO();
        return ResponseEntity.created
                        (URI.create
                                (ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString() + "/" + userResponseDTO.getId()))
                .body(userResponseDTO);
    }
}
