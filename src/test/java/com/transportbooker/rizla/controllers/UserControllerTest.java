package com.transportbooker.rizla.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transportbooker.rizla.dto.request.LoginRequestDTO;
import com.transportbooker.rizla.dto.request.UserRequestDTO;
import com.transportbooker.rizla.util.BaseSetupTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest extends BaseSetupTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    private static final String INIT_SQL = "data/init_data.sql";
//
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//            "postgres:10.0"
//    ).withDatabaseName("rizladb")
//            .withUsername("rizla_user")
//            .withPassword("rizla_pass")
//            .withInitScript(INIT_SQL)
//            .withReuse(true);
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//    }
//
//    @BeforeAll
//    public static void beforeAll() {
//        postgres.start();
//    }
//    @AfterAll
//    public static void afterAll() {
//        postgres.stop();
//    }

    @Test
    @Order(1)
    public void registerUser_Expect_201_Created() throws Exception {
        UserRequestDTO userRequestDTO = createPassengerUser("rbk@gmail.com","Rodney1","PASSENGER");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/public/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    @Order(2)
    public void loginUser_Expect_200_StatusCode() throws Exception {
        UserRequestDTO userRequestDTO = createPassengerUser("yh@yh.com","Yeah","PASSENGER");



        //UserRequestDTO userRequestDTO = createPassengerUser("PASSENGER");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/public/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequestDTO))).andReturn();

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername(userRequestDTO.getUsername());
        loginRequestDTO.setPassword(userRequestDTO.getPassword());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/public/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


}