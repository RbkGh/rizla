package com.transportbooker.rizla.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transportbooker.rizla.dto.request.LoginRequestDTO;
import com.transportbooker.rizla.dto.request.UserRequestDTO;
import com.transportbooker.rizla.dto.response.LoginResponseDTO;
import com.transportbooker.rizla.dto.response.UserResponseDTO;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseSetupTest {
    @Autowired
    public MockMvc mockMvc;

    private static final String INIT_SQL = "data/init_data.sql";

    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:10.0"
    ).withDatabaseName("rizladb")
            .withUsername("rizla_user")
            .withPassword("rizla_pass")
            .withInitScript(INIT_SQL)
            .withReuse(true);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    public  static void beforeAll() {
        postgres.start();
    }
    @AfterAll
    public  static void afterAll() {
        //postgres.stop();
    }


    public UserRequestDTO createPassengerUser(String username, String name, String userType) {
        return UserRequestDTO.builder()
                .username(username)
                .password("rizla_pass")
                .name(name)
                .userType(userType)
                .build();
    }

    public String getJWT(String username, String password) throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();

        if(username != null && password != null) {
            loginRequestDTO.setUsername(username);
            loginRequestDTO.setPassword(password);
        } else {
            UserRequestDTO userRequestDTO = createPassengerUser("s2"+RandomStringUtils.randomAlphabetic(3,6)+"@yh.com","wrt","PASSENGER");


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

    public UserResponseDTO createUserSavedInDB(UserRequestDTO userRequestDTO) throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/public/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRequestDTO))).andReturn();

        UserResponseDTO userResponseDTO = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), UserResponseDTO.class);

        return userResponseDTO;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
