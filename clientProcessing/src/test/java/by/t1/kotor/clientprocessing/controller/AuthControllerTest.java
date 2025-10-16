package by.t1.kotor.clientprocessing.controller;

import by.t1.kotor.clientprocessing.model.dto.security.JwtAuthenticationDto;
import by.t1.kotor.clientprocessing.model.dto.security.RefreshTokenDto;
import by.t1.kotor.clientprocessing.model.dto.security.UserCredentialDto;
import by.t1.kotor.clientprocessing.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"test-cards"})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signInTest() throws Exception {
        UserCredentialDto userCredentialDto = UserCredentialDto.builder()
                .email("user1@example.com")
                .password("pass1")
                .build();

        String userJson = objectMapper.writeValueAsString(userCredentialDto);

        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAuthenticationDto jwtAuthenticationDto = objectMapper.readValue(tokenJson, JwtAuthenticationDto.class);

        Assertions.assertEquals(userCredentialDto.getEmail(), jwtService.getEmailFromToken(jwtAuthenticationDto.getToken()));
    }

    @Test
    void signInNegativeTest() throws Exception {
        UserCredentialDto userCredentialDto = UserCredentialDto.builder()
                .email("user1@example.com")
                .password("1233333")
                .build();

        String userJson = objectMapper.writeValueAsString(userCredentialDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refresh() throws Exception {
        UserCredentialDto userCredentialDto = UserCredentialDto.builder()
                .email("user1@example.com")
                .password("pass1")
                .build();

        String userJson = objectMapper.writeValueAsString(userCredentialDto);

        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();

        refreshTokenDto.setRefreshToken(objectMapper.readValue(tokenJson, JwtAuthenticationDto.class).getRefreshToken());

        String refreshTokenJson = objectMapper.writeValueAsString(refreshTokenDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(refreshTokenJson))
                .andExpect(status().isOk());
    }
}