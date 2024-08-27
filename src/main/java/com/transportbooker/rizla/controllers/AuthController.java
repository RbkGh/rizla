package com.transportbooker.rizla.controllers;

import com.transportbooker.rizla.dto.request.LoginRequestDTO;
import com.transportbooker.rizla.dto.response.LoginResponseDTO;
import com.transportbooker.rizla.services.AuthService;
import com.transportbooker.rizla.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/public/auth")
public class AuthController {

    private UserService userService;
    private AuthService authService;;

    public AuthController(UserService userService,AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequestDTO authenticationRequest) throws Exception {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        String jwtToken = authService.createAuthenticationToken(username, password);

        return ResponseEntity.ok(LoginResponseDTO.builder().jwtToken(jwtToken));
    }
}
