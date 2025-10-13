package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.exception.BlacklistedClientException;
import by.t1.kotor.clientprocessing.exception.ClientNotFoundException;
import by.t1.kotor.clientprocessing.mapper.ClientMapper;
import by.t1.kotor.clientprocessing.model.Client;
import by.t1.kotor.clientprocessing.model.Role;
import by.t1.kotor.clientprocessing.model.User;
import by.t1.kotor.clientprocessing.model.dto.client.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.client.ClientResponse;
import by.t1.kotor.clientprocessing.model.enums.DocumentTypeEnum;
import by.t1.kotor.clientprocessing.model.enums.RoleEnum;
import by.t1.kotor.clientprocessing.repository.BlacklistRegistryRepository;
import by.t1.kotor.clientprocessing.repository.ClientRepository;
import by.t1.kotor.clientprocessing.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private BlacklistRegistryRepository blacklistRegistryRepository;
    @Mock
    private UserService userService;
    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    private ClientRegistrationRequest buildRequest() {
        return ClientRegistrationRequest.builder()
                .firstName("John")
                .middleName("M")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .documentType(DocumentTypeEnum.PASSPORT)
                .documentId("AB1234567")
                .documentPrefix("AB")
                .documentSuffix("567")
                .login("johndoe")
                .email("john@example.com")
                .password("password")
                .build();
    }

    @Test
    void registerClient_shouldThrowException_whenClientIsBlacklisted() {
        ClientRegistrationRequest request = buildRequest();
        when(blacklistRegistryRepository.existsBlacklistRegistriesByDocumentTypeAndDocumentId(
                request.documentType(), request.documentId())
        ).thenReturn(true);

        assertThrows(BlacklistedClientException.class, () ->
                clientService.registerClient(request, Collections.emptySet())
        );

        verify(clientRepository, never()).save(any());
    }


    @Test
    void registerClient_shouldRegisterClientSuccessfully() {
        ClientRegistrationRequest request = buildRequest();
        Client client = new Client();
        User user = new User();
        ClientResponse response = ClientResponse.builder().build();

        when(blacklistRegistryRepository.existsBlacklistRegistriesByDocumentTypeAndDocumentId(
                request.documentType(), request.documentId())
        ).thenReturn(false);
        when(clientMapper.toEntity(request)).thenReturn(client);
        when(userService.create(eq(request), any())).thenReturn(user);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(response);

        ClientResponse result = clientService.registerClient(request, Set.of(new Role(RoleEnum.CURRENT_CLIENT)));

        assertNotNull(result);
        assertEquals(response, result);
        verify(clientMapper).toEntity(request);
        verify(userService).create(eq(request), any());
        verify(clientRepository).save(client);
        verify(clientMapper).toDto(client);
    }

    @Test
    void blockClient_shouldThrowException_whenClientNotFound() {
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.blockClient(clientId));

        verify(clientRepository, never()).save(any());
    }

    @Test
    void blockClient_shouldSetBlockedRoleAndSaveClient_whenClientExists() {
        Long clientId = 1L;
        User user = new User();
        user.setRoles(Set.of(new Role(RoleEnum.BLOCKED_CLIENT)));

        Client client = new Client();
        client.setUser(user);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        clientService.blockClient(clientId);

        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("BLOCKED_CLIENT")));

        verify(clientRepository).save(client);
    }

}
