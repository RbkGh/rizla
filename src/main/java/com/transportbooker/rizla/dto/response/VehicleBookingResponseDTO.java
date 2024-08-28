package com.transportbooker.rizla.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleBookingResponseDTO {

    private Long id;

    private Long passengerID;
    private Long vehicleID;

    private String vehicleManufacturer;

    private String vehicleModel;

    private String vehicleLicenseNumber;

    private String bookingRequestTime;

    private String bookingConfirmedTime;

    private String bookingStartTime;

    private String bookingEndTime;

}
