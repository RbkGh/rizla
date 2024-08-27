package com.transportbooker.rizla.controllers;

import com.transportbooker.rizla.dto.response.VehicleBookingResponseDTO;
import com.transportbooker.rizla.exceptions.NotFoundHttpException;
import com.transportbooker.rizla.services.VehicleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicles")
@Slf4j
@AllArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<?> getCars() throws NotFoundHttpException {

        return ResponseEntity.ok(vehicleService.getAllVehicles().stream().map(vehicle -> VehicleBookingResponseDTO.builder()
                .id(vehicle.getId())
                .vehicleLicenseNumber(vehicle.getVehicleLicenseNumber())
                .vehicleManufacturer(vehicle.getVehicleManufacturer())
                .vehicleModel(vehicle.getVehicleModel())
                .build()).collect(Collectors.toList()));
    }

}
