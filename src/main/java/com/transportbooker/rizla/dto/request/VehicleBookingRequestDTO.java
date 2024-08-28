package com.transportbooker.rizla.dto.request;

import com.transportbooker.rizla.models.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleBookingRequestDTO {

    private TimeSlot timeSlot;

}
