package com.transportbooker.rizla.services;

import com.transportbooker.rizla.dto.request.VehicleBookingRequestDTO;
import com.transportbooker.rizla.exceptions.NotFoundHttpException;
import com.transportbooker.rizla.models.CustomUser;
import com.transportbooker.rizla.models.VehicleBooking;
import com.transportbooker.rizla.repository.UserRepository;
import com.transportbooker.rizla.repository.VehicleBookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VehicleBookingService {

    private VehicleBookingRepository vehicleBookingRepository;
    private final UserRepository userRepository;

    public VehicleBooking createVehicleBooking(CustomUser customUser,VehicleBookingRequestDTO vehicleBookingRequestDTO) throws NotFoundHttpException {
        return null;
    }


}
