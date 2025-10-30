package by.t1.kotor.clientprocessing.controller;

import by.t1.kotor.clientprocessing.kafka.KafkaProducer;
import by.t1.kotor.clientprocessing.model.dto.card.CardRequest;
import by.t1.kotor.clientprocessing.model.dto.security.JwtAuthenticationDto;
import by.t1.kotor.clientprocessing.model.dto.security.UserCredentialDto;
import by.t1.kotor.clientprocessing.service.impl.CardRequestService;
import by.t1.kotor.common.model.dto.CardMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"test-cards"})
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KafkaProducer<CardMessage> kafkaProducer;

    private String getAccessToken() throws Exception {
        UserCredentialDto userCredentialDto = UserCredentialDto.builder()
                .email("user1@example.com")
                .password("pass1")
                .build();

        String loginJson = objectMapper.writeValueAsString(userCredentialDto);

        String tokens = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAuthenticationDto jwtAuthenticationDto = objectMapper.readValue(tokens, JwtAuthenticationDto.class);
        return jwtAuthenticationDto.getToken();
    }

    @Test
    void createCard() throws Exception {
        String token = getAccessToken();

        CardRequest cardRequest = CardRequest.builder()
                .clientId(1L)
                .productId(1L)
                .cardType("VISA")
                .build();

        String cardJson = objectMapper.writeValueAsString(cardRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cards")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cardJson))
                .andExpect(status().isAccepted());

        Mockito.verify(kafkaProducer, Mockito.times(1))
                .sendTo(Mockito.anyString(), Mockito.any(CardMessage.class));
    }
}