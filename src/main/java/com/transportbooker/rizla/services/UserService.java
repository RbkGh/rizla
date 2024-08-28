package com.transportbooker.rizla.services;

import com.transportbooker.rizla.models.CustomUser;
import com.transportbooker.rizla.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    /**
     * save user in db with password field hashed with PasswordEncoder
     *
     * @param customUser
     * @return
     */
    public CustomUser saveUser(CustomUser customUser) {
        customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));
        return userRepository.save(customUser);
    }

    public Optional<CustomUser> findUserByID(Long userID) throws EntityNotFoundException {
        return userRepository.findById(userID);
    }

    public Optional<CustomUser> findUserByUserName(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username);
    }
}
