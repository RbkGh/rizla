package com.transportbooker.rizla.services;

import com.transportbooker.rizla.models.*;
import com.transportbooker.rizla.repository.VehicleBookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class VehicleBookingServiceTest {

    @Mock
    private VehicleBookingRepository vehicleBookingRepository;

    @InjectMocks
    private VehicleBookingService vehicleBookingService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void overrideVehicleBooking_ExpectTrue() {
        LocalDateTime localDateTimeToCheckAgainst = LocalDateTime.now().withHour(6).withMinute(31);

        VehicleBookingStartAndEndTimeHolder vehicleBookingStartAndEndTimeHolder =
                vehicleBookingService.getTImeUnitTimeFromRequest(TimeSlot.HOUR_2);

        long customerId = 5L;
        long vehicleId = 3L;
        long vehicleBookingId = 4L;
        Optional<VehicleBooking> vehicleBooking = Optional.of(VehicleBooking
                .builder()
                .id(vehicleBookingId)
                .passenger(CustomUser.builder().id(customerId).build())
                .bookingStartTime(vehicleBookingStartAndEndTimeHolder.getBookingStartTime())
                .vehicle(Vehicle.builder().id(vehicleId).build())
                .confirmed(false)
                .build());

        LocalDateTime bookingStartTime = vehicleBookingStartAndEndTimeHolder.getBookingStartTime();

        Mockito.when(vehicleBookingRepository.findByVehicle_IdAndBookingStartTime(vehicleId, bookingStartTime))
                .thenReturn(vehicleBooking);



        assertTrue(vehicleBookingService.canExecutiveOverrideVehicleBooking(
                vehicleBooking.get(), localDateTimeToCheckAgainst, bookingStartTime));


    }

    @Test
    public void overrideVehicleBooking_ExpectFalse() {
        LocalDateTime localDateTimeToCheckAgainst = LocalDateTime.now().withHour(6).withMinute(10);

        VehicleBookingStartAndEndTimeHolder vehicleBookingStartAndEndTimeHolder =
                vehicleBookingService.getTImeUnitTimeFromRequest(TimeSlot.HOUR_2);

        long customerId = 5L;
        long vehicleId = 3L;
        long vehicleBookingId = 4L;
        Optional<VehicleBooking> vehicleBooking = Optional.of(VehicleBooking
                .builder()
                .id(vehicleBookingId)
                .passenger(CustomUser.builder().id(customerId).build())
                .bookingStartTime(vehicleBookingStartAndEndTimeHolder.getBookingStartTime())
                .vehicle(Vehicle.builder().id(vehicleId).build())
                .confirmed(false)
                .build());

        LocalDateTime bookingStartTime = vehicleBookingStartAndEndTimeHolder.getBookingStartTime();

        Mockito.when(vehicleBookingRepository.findByVehicle_IdAndBookingStartTime(vehicleId, bookingStartTime))
                .thenReturn(vehicleBooking);



        assertFalse(vehicleBookingService.canExecutiveOverrideVehicleBooking(
                vehicleBooking.get(), localDateTimeToCheckAgainst, bookingStartTime));


    }
}