package com.transportbooker.rizla.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class VehicleBookingResponseDTO {

    private Long id;

    private String vehicleManufacturer;

    private String vehicleModel;

    private String vehicleLicenseNumber;

}
