package com.transportbooker.rizla.services;

import com.transportbooker.rizla.models.CustomUser;
import com.transportbooker.rizla.repository.UserRepository;
import com.transportbooker.rizla.util.JWTTokenUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    //private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
   // private JWTTokenUtil jwtTokenUtil;

//    public String createAuthenticationToken(String username, String password) throws Exception {
//        authenticate(username, password);
//        UserDetails userDetails = loadUserByUsername(username);
//        String token = jwtTokenUtil.generateToken(userDetails);
//        return token;
//    }
//
//    private void authenticate(String username, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        CustomUser customUser = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//
//        return new User(customUser.getUsername(), customUser.getPassword(),
//                customUser.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//    }

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
}
