package com.transportbooker.rizla.controllers;

import com.transportbooker.rizla.dto.request.VehicleBookingRequestDTO;
import com.transportbooker.rizla.dto.response.VehicleBookingResponseDTO;
import com.transportbooker.rizla.exceptions.NotFoundHttpException;
import com.transportbooker.rizla.models.CustomUser;
import com.transportbooker.rizla.models.Vehicle;
import com.transportbooker.rizla.models.VehicleBooking;
import com.transportbooker.rizla.models.VehicleBookingStartAndEndTimeHolder;
import com.transportbooker.rizla.services.UserService;
import com.transportbooker.rizla.services.VehicleBookingService;
import com.transportbooker.rizla.services.VehicleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@Slf4j
@AllArgsConstructor
public class VehicleBookingController {

    private final UserService userService;
    private final VehicleBookingService vehicleBookingService;
    private final VehicleService vehicleService;

    @PostMapping("/{vehicleID}/car/{passengerID}/user")
    public ResponseEntity<?> createBooking(@PathVariable Long vehicleID, @PathVariable Long passengerID, @RequestBody VehicleBookingRequestDTO vehicleBookingRequestDTO) throws NotFoundHttpException {
        Optional<CustomUser> customUser = userService.findUserByID(passengerID);
        Optional<Vehicle> vehicle = vehicleService.findVehicleByID(vehicleID);

        if (customUser.isEmpty())
            throw new NotFoundHttpException();
        if (vehicle.isEmpty())
            throw new NotFoundHttpException();

        VehicleBookingStartAndEndTimeHolder vehicleBookingStartAndEndTimeHolder =
                vehicleBookingService.getTImeUnitTimeFromRequest(vehicleBookingRequestDTO.getTimeSlot());

        if (vehicleBookingService.doesVehicleBookingExist(vehicle.get(), vehicleBookingStartAndEndTimeHolder.getBookingStartTime()))
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body("Vehicle Booking For Exact Slot Exists");


        VehicleBooking vehicleBooking = vehicleBookingService.createVehicleBooking(customUser.get(), vehicle.get(), vehicleBookingRequestDTO);

        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(VehicleBookingResponseDTO.builder()
                .id(vehicleBooking.getId())
                .bookingRequestTime(vehicleBooking.getBookingRequestTime().toString())
                .passengerID(vehicleBooking.getPassenger().getId())
                .vehicleID(vehicleBooking.getVehicle().getId())
                .vehicleLicenseNumber(vehicleBooking.getVehicle().getVehicleLicenseNumber())
                .build());
    }

    @PutMapping("/executive/{vehicleBookingID}")
    @PreAuthorize("hasRole('ROLE_EXECPASSENGER')")
    public ResponseEntity<?> createBookingExecutivePassenger(@PathVariable Long vehicleBookingID, @RequestBody VehicleBookingRequestDTO vehicleBookingRequestDTO) throws NotFoundHttpException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        Optional<CustomUser> customUser = userService.findUserByUserName(username);

        Optional<VehicleBooking> vehicleBookingInDB = vehicleBookingService.findVehicleBookingByVehicleBookingID(vehicleBookingID);

        if (vehicleBookingInDB.isEmpty())
            throw new NotFoundHttpException();

        VehicleBookingStartAndEndTimeHolder vehicleBookingStartAndEndTimeHolder =
                vehicleBookingService.getTImeUnitTimeFromRequest(vehicleBookingRequestDTO.getTimeSlot());

        if (!vehicleBookingService.canExecutiveOverrideVehicleBooking(vehicleBookingInDB.get(), vehicleBookingStartAndEndTimeHolder.getBookingStartTime()))
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body("Vehicle cannot be booked since time to book is more than 30 minutes");


        Optional<VehicleBooking> vehicleBookingOverriden = vehicleBookingService.overrideVehicleBooking(customUser.get(), vehicleBookingInDB.get());

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{vehicleBookingID}/confirm")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<?> confirmBooking(@PathVariable Long vehicleBookingID) throws NotFoundHttpException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        Optional<CustomUser> customUser = userService.findUserByUserName(username);

        if (!vehicleBookingService.doesVehicleBookingExist(vehicleBookingID))
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Vehicle Booking doesn't exist");


        vehicleBookingService.confirmVehicleBooking(
                customUser.get(),
                vehicleBookingService.getVehicleBookingById(vehicleBookingID).get()).get();


        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{vehicleBookingID}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long vehicleBookingID) throws NotFoundHttpException {

        if (!vehicleBookingService.doesVehicleBookingExist(vehicleBookingID))
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Vehicle Booking doesn't exist");


        vehicleBookingService.cancelVehicleBooking(
                vehicleBookingService.getVehicleBookingById(vehicleBookingID).get());


        return ResponseEntity.noContent().build();
    }

}
