package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.exception.BlacklistedClientException;
import by.t1.kotor.clientprocessing.exception.ClientNotFoundException;
import by.t1.kotor.clientprocessing.mapper.ClientMapper;
import by.t1.kotor.clientprocessing.mapper.UserMapper;
import by.t1.kotor.clientprocessing.model.Client;
import by.t1.kotor.clientprocessing.model.Role;
import by.t1.kotor.clientprocessing.model.User;
import by.t1.kotor.clientprocessing.model.dto.client.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.client.ClientResponse;
import by.t1.kotor.clientprocessing.model.enums.RoleEnum;
import by.t1.kotor.clientprocessing.repository.BlacklistRegistryRepository;
import by.t1.kotor.clientprocessing.repository.ClientRepository;
import by.t1.kotor.clientprocessing.repository.UserRepository;
import by.t1.kotor.clientprocessing.service.ClientService;
import by.t1.kotor.clientprocessing.service.RoleRepository;
import by.t1.kotor.clientprocessing.service.UserService;
import by.t1.kotor.crosscuttingstarter.aop.annotation.LogDatasourceError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final BlacklistRegistryRepository blacklistRegistryRepository;
    private final UserService userService;
    private final ClientMapper clientMapper;
    private final RoleRepository roleRepository;

    @Override
    @LogDatasourceError
    public ClientResponse registerClient(ClientRegistrationRequest request, Set<Role> roles) {
        log.info("Registering new client: {}", request);

        if (blacklistRegistryRepository.existsBlacklistRegistriesByDocumentTypeAndDocumentId(
                request.documentType(), request.documentId())) {
            log.warn("Attempt to register blacklisted client: {}", request);
            throw new BlacklistedClientException(request.documentId());
        }

        Client client = clientMapper.toEntity(request);
        User user = userService.create(request, roles);
        log.debug("Saved User entity: {}", user);

        client.setUser(user);
        Client savedClient = clientRepository.save(client);
        log.info("Client registered successfully: {}", savedClient);

        return clientMapper.toDto(client);
    }

    @Override
    @LogDatasourceError
    @Transactional(readOnly = true)
    public ClientResponse findById(Long id) {
        log.info("Fetching client by id={}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Client not found: id={}", id);
                    return new ClientNotFoundException(id);
                });
        return clientMapper.toDto(client);
    }

    @Override
    public void blockClient(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    log.warn("Client not found: id={}", clientId);
                    return new ClientNotFoundException(clientId);
                });

        client.getUser().setRoles(Set.of(new Role(RoleEnum.BLOCKED_CLIENT)));
        clientRepository.save(client);
    }
}
