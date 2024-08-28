package com.transportbooker.rizla.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transportbooker.rizla.dto.request.LoginRequestDTO;
import com.transportbooker.rizla.dto.request.UserRequestDTO;
import com.transportbooker.rizla.dto.response.LoginResponseDTO;
import com.transportbooker.rizla.util.BaseSetupTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleControllerTest extends BaseSetupTest {

    public String JWT;

    @Test
    public void getCars_ExpectSuccess() throws Exception {
        JWT = getJWT(null,null);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + JWT))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}