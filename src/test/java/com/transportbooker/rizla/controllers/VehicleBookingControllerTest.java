package com.transportbooker.rizla.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.transportbooker.rizla.dto.request.UserRequestDTO;
import com.transportbooker.rizla.dto.request.VehicleBookingRequestDTO;
import com.transportbooker.rizla.dto.response.UserResponseDTO;
import com.transportbooker.rizla.dto.response.VehicleBookingResponseDTO;
import com.transportbooker.rizla.models.TimeSlot;
import com.transportbooker.rizla.models.Vehicle;
import com.transportbooker.rizla.models.VehicleBooking;
import com.transportbooker.rizla.models.VehicleBookingStartAndEndTimeHolder;
import com.transportbooker.rizla.repository.UserRepository;
import com.transportbooker.rizla.repository.VehicleBookingRepository;
import com.transportbooker.rizla.repository.VehicleRepository;
import com.transportbooker.rizla.services.VehicleBookingService;
import com.transportbooker.rizla.util.BaseSetupTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class VehicleBookingControllerTest extends BaseSetupTest {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VehicleBookingRepository vehicleBookingRepository;
    @Autowired
    private VehicleBookingService vehicleBookingService;

    @Test
    void createBooking_TestSuccessful_AndDuplicateBooking() throws Exception {
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

        this.mockMvc.perform(MockMvcRequestBuilders.post( "/api/bookings/"+carId+"/car/"+userId+"/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(VehicleBookingRequestDTO.builder()
                                .timeSlot(TimeSlot.HOUR_1).build()))
                        .header("Authorization", "Bearer " + JWT))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //try again, expect 409 conflict
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/bookings/"+carId+"/car/"+userId+"/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(VehicleBookingRequestDTO.builder()
                                .timeSlot(TimeSlot.HOUR_1).build()))
                        .header("Authorization", "Bearer " + JWT))
                .andExpect(MockMvcResultMatchers.status().isConflict()); //we expect a conflict here

    }


    @Test
    void overrideBooking_ExecutivePassenger_WithAbilityToOverridePassengersBookingExpectTimeConstraintNotMet() throws Exception {
        UserRequestDTO normalPassenger = UserRequestDTO.builder()
                .userType("PASSENGER")
                .username(RandomStringUtils.randomAlphabetic(3,6)+"yes@gmail.com") //random unique email
                .password("pass1234")
                .name("Rodey")
                .build();

        UserResponseDTO userResponseDTO = createUserSavedInDB(normalPassenger);
        Long userId = userResponseDTO.getId();

        String JWT = getJWT(normalPassenger.getUsername(), normalPassenger.getPassword());

        Vehicle vehicle = Vehicle.builder()
                .vehicleManufacturer("Tesla")
                .vehicleModel("Model S")
                .vehicleLicenseNumber("3222p")
                .build();
        Vehicle vehicleSaved = vehicleRepository.save(vehicle);


        String carId = vehicleSaved.getId().toString();

        MockHttpServletResponse mockHttpServletResponse = this.mockMvc.perform(MockMvcRequestBuilders.post( "/api/bookings/"+carId+"/car/"+userId+"/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(VehicleBookingRequestDTO.builder()
                                .timeSlot(TimeSlot.HOUR_7).build()))
                        .header("Authorization", "Bearer " + JWT))
                .andReturn().getResponse();

        VehicleBookingResponseDTO vehicleBookingResponseDTO =
                new Gson().fromJson(mockHttpServletResponse.getContentAsString(),VehicleBookingResponseDTO.class);




        //now save an executive passenger into db and use that to override a booking that is not confirmed at least 30 minutes to time
        UserRequestDTO executivePassenger = UserRequestDTO.builder()
                .userType("EXEC_PASSENGER") //executive passenger
                .username(RandomStringUtils.randomAlphabetic(4)+"yes@gmail.com") //random unique email
                .password("pass1234")
                .name("Rodey")
                .build();

        UserResponseDTO executivePassengerSavedInDB = createUserSavedInDB(executivePassenger);
//        Long executivePassengerSavedInDBId = executivePassengerSavedInDB.getId();
        String EXECUTIVE_JWT = getJWT(executivePassenger.getUsername(), executivePassenger.getPassword());

        VehicleBookingRequestDTO vehicleBookingRequestDTO = VehicleBookingRequestDTO.builder()
                .timeSlot(TimeSlot.HOUR_7).build();
        VehicleBookingStartAndEndTimeHolder vehicleBookingStartAndEndTimeHolder = vehicleBookingService.getTImeUnitTimeFromRequest(vehicleBookingRequestDTO.getTimeSlot());

//        VehicleBooking vehicleBooking = VehicleBooking.builder()
//                .vehicle(vehicleSaved)
//                .passenger(userRepository.findByUsername(normalPassenger.getUsername()).get())
//                .bookingRequestTime(LocalDateTime.now())
//                .bookingStartTime(vehicleBookingStartAndEndTimeHolder.getBookingStartTime())
//                .bookingEndTime(vehicleBookingStartAndEndTimeHolder.getBookingEndTime())
//                .build();
//        VehicleBooking vehicleBookingSaved = vehicleBookingRepository.save(vehicleBooking);


        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/executive/"+vehicleBookingResponseDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vehicleBookingRequestDTO))
                        .header("Authorization", "Bearer " + EXECUTIVE_JWT))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    @Transactional
    void confirmBooking() throws Exception {
        //Create a driver that can confirm an existing booking
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .userType("DRIVER") //ONLY DRIVER CAN BOOK
                .username(RandomStringUtils.random(4)+"randomboy@test.com")
                .password("pass1234")
                .name("Rodey")
                .build();

        //save driver in db and get id
        UserResponseDTO userResponseDTO = createUserSavedInDB(userRequestDTO);
        Long userId = userResponseDTO.getId();

        //get jwt of driver
        String JWT = getJWT(userRequestDTO.getUsername(), userRequestDTO.getPassword());

        //save vehicle that will be used for booking by passenger
        Vehicle vehicle = Vehicle.builder()
                .vehicleManufacturer("Tesla")
                .vehicleModel("Model S")
                .vehicleLicenseNumber("3222p")
                .build();
        Vehicle vehicleSaved = vehicleRepository.save(vehicle);

        //save booking request with vehicle
        VehicleBookingRequestDTO vehicleBookingRequestDTO = VehicleBookingRequestDTO.builder()
                .timeSlot(TimeSlot.HOUR_1).build();
        VehicleBookingStartAndEndTimeHolder startTimeAndEndHolder = vehicleBookingService.getTImeUnitTimeFromRequest(vehicleBookingRequestDTO.getTimeSlot());

        VehicleBooking vehicleBooking = VehicleBooking.builder()
                .bookingStartTime(startTimeAndEndHolder.getBookingStartTime())
                .bookingEndTime(startTimeAndEndHolder.getBookingEndTime())
                .bookingRequestTime(LocalDateTime.now())
                .vehicle(vehicleSaved)
                .build();

        VehicleBooking vehicleBookingSaved = vehicleBookingRepository.save(vehicleBooking); //save booking request with vehicle
        long vehicleBookingId = vehicleBookingSaved.getId();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/"+vehicleBookingId+"/confirm" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + JWT))
                .andExpect(MockMvcResultMatchers.status().isNoContent());


    }


    @Test
    @Transactional
    void cancelBookingExpect_Success() throws Exception {

        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .userType("PASSENGER") // PASSENGER
                .username(RandomStringUtils.random(4)+"randomboy@test.com")
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

        //save booking request with vehicle
        VehicleBookingRequestDTO vehicleBookingRequestDTO = VehicleBookingRequestDTO.builder()
                .timeSlot(TimeSlot.HOUR_5).build();
        VehicleBookingStartAndEndTimeHolder startTimeAndEndHolder = vehicleBookingService.getTImeUnitTimeFromRequest(vehicleBookingRequestDTO.getTimeSlot());

        VehicleBooking vehicleBooking = VehicleBooking.builder()
                .bookingStartTime(startTimeAndEndHolder.getBookingStartTime())
                .bookingEndTime(startTimeAndEndHolder.getBookingEndTime())
                .bookingRequestTime(LocalDateTime.now())
                .vehicle(vehicleSaved)
                .build();

        VehicleBooking vehicleBookingSaved = vehicleBookingRepository.save(vehicleBooking); //save booking request with vehicle
        long vehicleBookingId = vehicleBookingSaved.getId();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/"+vehicleBookingId+"/cancel" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + JWT))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }
}