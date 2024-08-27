package com.transportbooker.rizla.controllers;

import com.transportbooker.rizla.dto.request.VehicleBookingRequestDTO;
import com.transportbooker.rizla.dto.response.VehicleBookingResponseDTO;
import com.transportbooker.rizla.exceptions.NotFoundHttpException;
import com.transportbooker.rizla.models.CustomUser;
import com.transportbooker.rizla.services.UserService;
import com.transportbooker.rizla.services.VehicleBookingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@Slf4j
@AllArgsConstructor
public class VehicleBookingController {

    private final UserService userService;
    private final VehicleBookingService vehicleBookingService;

    @PostMapping("/{passengerID}")
    public ResponseEntity<?> createBooking(@PathVariable Long passengerID, @RequestBody VehicleBookingRequestDTO vehicleBookingRequestDTO) throws NotFoundHttpException {
        Optional<CustomUser> customUser = userService.findUserByID(passengerID);

        if(customUser.isEmpty())
            throw new NotFoundHttpException();

        vehicleBookingService.createVehicleBooking(customUser.get(), vehicleBookingRequestDTO);

        return ResponseEntity.ok(VehicleBookingResponseDTO.builder().build());
    }

}
