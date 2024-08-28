package com.transportbooker.rizla.controllers;

import com.transportbooker.rizla.dto.request.UserRequestDTO;
import com.transportbooker.rizla.dto.request.VehicleBookingRequestDTO;
import com.transportbooker.rizla.dto.response.UserResponseDTO;
import com.transportbooker.rizla.models.TimeSlot;
import com.transportbooker.rizla.models.Vehicle;
import com.transportbooker.rizla.repository.VehicleRepository;
import com.transportbooker.rizla.util.BaseSetupTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
public class VehicleBookingControllerTest extends BaseSetupTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void createBooking() throws Exception {
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .userType("PASSENGER")
                .username("randomboy@test.com")
                .password("pass1234")
                .name("Rodey")
                .build();

        UserResponseDTO userResponseDTO = createUserSavedInDB(userRequestDTO);
        Long userId = userResponseDTO.getId();

        String JWT = getJWT(userRequestDTO.getUsername(), userRequestDTO.getPassword());

        Vehicle vehicle = Vehicle.builder()
                .vehicleManufacturer("Tesla")
                .vehicleModel("Model S")
                .vehicleLicenseNumber("3222p")
                .build();
        Vehicle vehicleSaved = vehicleRepository.save(vehicle);


        String carId = vehicleSaved.getId().toString();
//        VehicleBookingRequestDTO vehicleBookingRequestDTO = new VehicleBookingRequestDTO();
//        vehicleBookingRequestDTO.setTimeSlot(TimeSlot.HOUR_1);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/bookings/"+carId+"/car/"+userId+"/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(VehicleBookingRequestDTO.builder()
                                .timeSlot(TimeSlot.HOUR_1).build()))
                        .header("Authorization", "Bearer " + JWT))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}