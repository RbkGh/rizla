package com.transportbooker.rizla.services;

import com.transportbooker.rizla.dto.request.VehicleBookingRequestDTO;
import com.transportbooker.rizla.exceptions.NotFoundHttpException;
import com.transportbooker.rizla.models.*;
import com.transportbooker.rizla.repository.VehicleBookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VehicleBookingService {

    private VehicleBookingRepository vehicleBookingRepository;

    @Transactional
    public VehicleBooking createVehicleBooking(CustomUser customUser, Vehicle vehicle, VehicleBookingRequestDTO vehicleBookingRequestDTO) throws NotFoundHttpException {
        VehicleBookingStartAndEndTimeHolder startAndEndTimeHolder = getTImeUnitTimeFromRequest(vehicleBookingRequestDTO.getTimeSlot());

        VehicleBooking vehicleBooking = VehicleBooking
                .builder()
                .vehicle(vehicle)
                .bookingRequestTime(LocalDateTime.now())
                .bookingStartTime(startAndEndTimeHolder.getBookingStartTime())
                .bookingEndTime(startAndEndTimeHolder.getBookingEndTime())
                .passenger(customUser)
                .confirmed(false)
                .build();

        VehicleBooking vehicleBookingSaved = vehicleBookingRepository.save(vehicleBooking);

        return vehicleBookingSaved;
    }

    @Transactional
    public Optional<VehicleBooking> overrideVehicleBooking(CustomUser passenger, VehicleBooking vehicleBooking) {
        vehicleBooking.setPassenger(passenger); //replacing the passenger now

        return Optional.of(vehicleBookingRepository.save(vehicleBooking));
    }

    public Optional<VehicleBooking> findVehicleBookingByVehicleBookingID(Long vehicleBookingID) {
        return vehicleBookingRepository.findById(vehicleBookingID);
    }

    @Transactional
    public Optional<VehicleBooking> confirmVehicleBooking(CustomUser driver, VehicleBooking vehicleBooking) throws NotFoundHttpException {
        vehicleBooking.setConfirmed(true);
        vehicleBooking.setDriver(driver);
        vehicleBooking.setBookingConfirmedTime(LocalDateTime.now());

        return Optional.of(vehicleBookingRepository.save(vehicleBooking));
    }

    @Transactional
    public void cancelVehicleBooking(VehicleBooking vehicleBooking) {

        vehicleBookingRepository.delete(vehicleBooking);
    }

    public Optional<VehicleBooking> getVehicleBookingById(Long vehicleBookingId) {
        return vehicleBookingRepository.findById(vehicleBookingId);
    }

    public boolean doesVehicleBookingExist(Long vehicleID) throws NotFoundHttpException {
        Optional<VehicleBooking> vehicleBooking = vehicleBookingRepository.findById(vehicleID);
        return vehicleBooking.isPresent();
    }

    public boolean doesVehicleBookingExist(Vehicle vehicle, LocalDateTime bookingStartTime) {
        Optional<VehicleBooking> vehicleBooking = vehicleBookingRepository.findByVehicle_IdAndBookingStartTime(vehicle.getId(), bookingStartTime);
        return vehicleBooking.isPresent();
    }

    public boolean canExecutiveOverrideVehicleBooking(VehicleBooking vehicleBookingInDB, LocalDateTime timeOfRequestingOverride, LocalDateTime bookingStartTime) {
        Optional<VehicleBooking> vehicleBooking = vehicleBookingRepository.findByVehicle_IdAndBookingStartTime(vehicleBookingInDB.getVehicle().getId(), bookingStartTime);

        return vehicleBooking.map(booking -> {
            Duration duration = Duration.between(timeOfRequestingOverride, booking.getBookingStartTime());
            return timeOfRequestingOverride.isBefore(booking.getBookingStartTime())
                    && duration.toMinutes() <= 30
                    && !booking.isConfirmed();
        }).orElse(false);
    }

    public VehicleBookingStartAndEndTimeHolder getTImeUnitTimeFromRequest(TimeSlot timeSlot) {
        int startHour = switch (timeSlot) {
            case HOUR_1 -> 6;
            case HOUR_2 -> 7;
            case HOUR_3 -> 8;
            case HOUR_4 -> 9;
            case HOUR_5 -> 10;
            case HOUR_6 -> 11;
            case HOUR_7 -> 12;
            case HOUR_8 -> 13;
            default -> 6;
        };

        LocalDateTime bookingStartTime = getCustomTime(startHour, 0, 0);
        LocalDateTime bookingEndTime = getCustomTime(startHour, 59, 59);

        return VehicleBookingStartAndEndTimeHolder
                .builder()
                .bookingStartTime(bookingStartTime)
                .bookingEndTime(bookingEndTime)
                .build();
    }

    private LocalDateTime getCustomTime(int hourOfDay, int minute, int second) {
        //because we are setting a constant nanosecond time, we can query each time for duplication
        return LocalDateTime.now().withHour(hourOfDay).withMinute(minute).withSecond(second).withNano(300);
    }

}
