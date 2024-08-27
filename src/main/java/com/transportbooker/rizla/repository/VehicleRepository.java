package com.transportbooker.rizla.repository;

import com.transportbooker.rizla.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

}
