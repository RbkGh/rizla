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

//    @BeforeAll
//    public void setup() throws Exception {
//        JWT = getJWT(null,null);
//    }

    @Test
    public void getCars_ExpectSuccess() throws Exception {
        JWT = getJWT(null,null);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + JWT))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Make a post request to get jwt from header
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public String getJWT(String username, String password) throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();

        if(username != null && password != null) {
            loginRequestDTO.setUsername(username);
            loginRequestDTO.setPassword(password);
        } else {
            UserRequestDTO userRequestDTO = createPassengerUser("s2@yh.com","wrt","PASSENGER");


            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/public/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(userRequestDTO))).andReturn();

            loginRequestDTO.setUsername(userRequestDTO.getUsername());
            loginRequestDTO.setPassword(userRequestDTO.getPassword());
        }

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/public/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequestDTO))).andReturn();

        String responseBodyString = mvcResult.getResponse().getContentAsString();
        LoginResponseDTO jwtKeyObject = new ObjectMapper().readValue(responseBodyString,LoginResponseDTO.class);

        return jwtKeyObject.getJwtToken();
    }
}