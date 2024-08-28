package com.transportbooker.rizla.repository;

import com.transportbooker.rizla.models.VehicleBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VehicleBookingRepository extends JpaRepository<VehicleBooking, Long> {
    Optional<VehicleBooking> findByVehicle_IdAndBookingStartTime(Long vehicleId, LocalDateTime bookingStartTime);
}
