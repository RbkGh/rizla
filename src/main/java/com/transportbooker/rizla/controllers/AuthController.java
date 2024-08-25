package com.transportbooker.rizla.controllers;

import com.transportbooker.rizla.dto.LoginRequestDTO;
import com.transportbooker.rizla.dto.LoginResponseDTO;
import com.transportbooker.rizla.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequestDTO authenticationRequest) throws Exception {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        String jwtToken = userService.createAuthenticationToken(username, password);

        return ResponseEntity.ok(LoginResponseDTO.builder().jwtToken(jwtToken));
    }
}
