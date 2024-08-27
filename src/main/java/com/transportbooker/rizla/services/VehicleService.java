package com.transportbooker.rizla.services;

import com.transportbooker.rizla.dto.request.VehicleBookingRequestDTO;
import com.transportbooker.rizla.exceptions.NotFoundHttpException;
import com.transportbooker.rizla.models.CustomUser;
import com.transportbooker.rizla.models.Vehicle;
import com.transportbooker.rizla.models.VehicleBooking;
import com.transportbooker.rizla.repository.UserRepository;
import com.transportbooker.rizla.repository.VehicleBookingRepository;
import com.transportbooker.rizla.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VehicleService {

    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() throws NotFoundHttpException {
        return vehicleRepository.findAll();
    }

}
