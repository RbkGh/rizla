package com.transportbooker.rizla.dto.request;

import com.transportbooker.rizla.models.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class VehicleBookingRequestDTO {

    private TimeSlot timeSlot;

}
