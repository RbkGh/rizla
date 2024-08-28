package com.transportbooker.rizla.services;

import com.transportbooker.rizla.dto.request.VehicleBookingRequestDTO;
import com.transportbooker.rizla.exceptions.NotFoundHttpException;
import com.transportbooker.rizla.models.*;
import com.transportbooker.rizla.repository.UserRepository;
import com.transportbooker.rizla.repository.VehicleBookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VehicleBookingService {

    private VehicleBookingRepository vehicleBookingRepository;
    private final UserRepository userRepository;

    @Transactional
    public VehicleBooking createVehicleBooking(CustomUser customUser, Vehicle vehicle, VehicleBookingRequestDTO vehicleBookingRequestDTO) throws NotFoundHttpException {
        VehicleBookingStartAndEndTimeHolder startAndEndTimeHolder = getTImeUnitTimeFromRequest(vehicleBookingRequestDTO.getTimeSlot());

        VehicleBooking vehicleBooking = VehicleBooking.builder()
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
    public Optional<VehicleBooking> confirmVehicleBooking(CustomUser driver, VehicleBooking vehicleBooking) throws NotFoundHttpException {
        vehicleBooking.setConfirmed(true);
        vehicleBooking.setDriver(driver);
        vehicleBooking.setBookingConfirmedTime(LocalDateTime.now());

        return Optional.of(vehicleBookingRepository.save(vehicleBooking));
    }

    @Transactional
    public void cancelVehicleBooking(VehicleBooking vehicleBooking) throws NotFoundHttpException {

        vehicleBookingRepository.delete(vehicleBooking);
    }

    public Optional<VehicleBooking> getVehicleBookingById(Long vehicleBookingId) {
        return vehicleBookingRepository.findById(vehicleBookingId);
    }

    public boolean doesVehicleBookingExist(Long vehicleID) throws NotFoundHttpException {
        Optional<VehicleBooking> vehicleBooking = vehicleBookingRepository.findById(vehicleID);
        return vehicleBooking.isPresent();
    }

    public boolean doesVehicleBookingExist(Vehicle vehicle, LocalDateTime bookingStartTime) throws NotFoundHttpException {
        Optional<VehicleBooking> vehicleBooking = vehicleBookingRepository.findByVehicle_IdAndBookingStartTime(vehicle.getId(), bookingStartTime);
        return vehicleBooking.isPresent();
    }

    public VehicleBookingStartAndEndTimeHolder getTImeUnitTimeFromRequest(TimeSlot timeSlot) {

        LocalDateTime bookingStartTime;
        LocalDateTime bookingEndTime;

        switch (timeSlot) {
            case HOUR_1: //set 7:00AM TO 7:59AM
                bookingStartTime = getCustomTime(6, 0, 0);
                bookingEndTime = getCustomTime(6, 59, 59);
                break;
            case HOUR_2: //set 8:00:00AM TO 8:59:59AM
                bookingStartTime = getCustomTime(7, 0, 0);
                bookingEndTime = getCustomTime(7, 59, 59);
                break;
            case HOUR_3: //set 9:00:00AM TO 9:59:59AM
                bookingStartTime = getCustomTime(9, 0, 0);
                bookingEndTime = getCustomTime(9, 59, 59);
                break;
            case HOUR_4: //set 10:00:00AM TO 10:59:59AM
                bookingStartTime = getCustomTime(10, 0, 0);
                bookingEndTime = getCustomTime(10, 59, 59);
                break;
            case HOUR_5: //set 11:00:00AM TO 11:59:59AM
                bookingStartTime = getCustomTime(11, 0, 0);
                bookingEndTime = getCustomTime(11, 59, 59);
                break;
            case HOUR_6: //set 12:00:00PM TO 12:59:59PM
                bookingStartTime = getCustomTime(12, 0, 0);
                bookingEndTime = getCustomTime(12, 59, 59);
                break;
            case HOUR_7: //set 1:00:00PM TO 1:59:59PM
                bookingStartTime = getCustomTime(13, 0, 0);
                bookingEndTime = getCustomTime(13, 59, 59);
                break;
            case HOUR_8: //set 2:00:00PM TO 2:59:59PM
                bookingStartTime = getCustomTime(14, 0, 0);
                bookingEndTime = getCustomTime(14, 59, 59);
                break;
            default: //default time is 7am to 7:59am
                bookingStartTime = getCustomTime(6, 0, 0);
                bookingEndTime = getCustomTime(6, 59, 59);
                break;
        }


        return VehicleBookingStartAndEndTimeHolder.builder()
                .bookingStartTime(bookingStartTime)
                .bookingEndTime(bookingEndTime)
                .build();
    }

    private LocalDateTime getCustomTime(int hourOfDay, int minute, int second) {
        //because we are setting a constant nanosecond time, we can query each time for duplication
        return LocalDateTime.now().withHour(hourOfDay).withMinute(minute).withSecond(second).withNano(300);
    }

}
