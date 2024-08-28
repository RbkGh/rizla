package com.transportbooker.rizla.controllers;

import com.transportbooker.rizla.dto.request.UserRequestDTO;
import com.transportbooker.rizla.dto.request.VehicleBookingRequestDTO;
import com.transportbooker.rizla.dto.response.UserResponseDTO;
import com.transportbooker.rizla.models.TimeSlot;
import com.transportbooker.rizla.util.BaseSetupTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
public class VehicleBookingControllerTest extends BaseSetupTest {

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

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/bookings/3/car/"+userId+"/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(VehicleBookingRequestDTO.builder()
                                .timeSlot(TimeSlot.HOUR_1)))
                        .header("Authorization", "Bearer " + JWT))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}