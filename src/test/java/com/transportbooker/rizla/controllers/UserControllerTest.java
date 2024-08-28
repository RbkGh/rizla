package com.transportbooker.rizla.controllers;

import com.transportbooker.rizla.dto.request.LoginRequestDTO;
import com.transportbooker.rizla.dto.request.UserRequestDTO;
import com.transportbooker.rizla.util.BaseSetupTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;


class UserControllerTest extends BaseSetupTest {

    @Test
    public void registerUser_Expect_201_Created() throws Exception {
        UserRequestDTO userRequestDTO = createPassengerUser(RandomStringUtils.randomAlphabetic(3,6)+"rbk@gmail.com", "Rodney1", "PASSENGER");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/public/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void loginUser_Expect_200_StatusCode() throws Exception {
        UserRequestDTO userRequestDTO = createPassengerUser(RandomStringUtils.randomAlphabetic(3,6)+"yh@yh.com", "Yeah", "PASSENGER");


        //UserRequestDTO userRequestDTO = createPassengerUser("PASSENGER");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/public/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRequestDTO))).andReturn();

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername(userRequestDTO.getUsername());
        loginRequestDTO.setPassword(userRequestDTO.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/public/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


}