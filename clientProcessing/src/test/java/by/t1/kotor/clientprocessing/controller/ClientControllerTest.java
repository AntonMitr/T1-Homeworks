package by.t1.kotor.clientprocessing.controller;

import by.t1.kotor.clientprocessing.model.dto.client.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.security.JwtAuthenticationDto;
import by.t1.kotor.clientprocessing.model.dto.security.UserCredentialDto;
import by.t1.kotor.clientprocessing.model.enums.DocumentTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"test-cards"})
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void register() throws Exception {
        ClientRegistrationRequest clientRequest = ClientRegistrationRequest.builder()
                .firstName("nameRegisterTest")
                .middleName("middleNameRegisterTest")
                .lastName("lastNameRegisterTest")
                .dateOfBirth(LocalDate.now())
                .documentType(DocumentTypeEnum.PASSPORT)
                .documentId("123")
                .documentPrefix("MC")
                .documentSuffix("BY")
                .login("loginRegisterTest")
                .email("testRegister@gmail.com")
                .password("test123")
                .build();

        String clientJson = objectMapper.writeValueAsString(clientRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andExpect(status().isOk());
    }

    @Test
    void getById() throws Exception {
        String token = getAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/1")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }
}