package com.transportbooker.rizla.repository;

import com.transportbooker.rizla.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface UserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsername(String username);
}
