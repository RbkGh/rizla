package com.transportbooker.rizla.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transportbooker.rizla.dto.request.UserRequestDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseSetupTest {
    @Autowired
    public MockMvc mockMvc;

    private static final String INIT_SQL = "data/init_data.sql";

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:10.0"
    ).withDatabaseName("rizladb")
            .withUsername("rizla_user")
            .withPassword("rizla_pass")
            .withInitScript(INIT_SQL)
            .withReuse(false);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }
    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }


    public UserRequestDTO createPassengerUser(String username, String name, String userType) {
        return UserRequestDTO.builder()
                .username(username)
                .password("rizla_pass")
                .name(name)
                .userType(userType)
                .build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
