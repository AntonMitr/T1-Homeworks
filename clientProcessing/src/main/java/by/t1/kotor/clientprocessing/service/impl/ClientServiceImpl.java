package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.exception.BlacklistedClientException;
import by.t1.kotor.clientprocessing.exception.ClientNotFoundException;
import by.t1.kotor.clientprocessing.mapper.ClientMapper;
import by.t1.kotor.clientprocessing.mapper.UserMapper;
import by.t1.kotor.clientprocessing.model.Client;
import by.t1.kotor.clientprocessing.model.User;
import by.t1.kotor.clientprocessing.model.dto.client.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.client.ClientResponse;
import by.t1.kotor.clientprocessing.repository.BlacklistRegistryRepository;
import by.t1.kotor.clientprocessing.repository.ClientRepository;
import by.t1.kotor.clientprocessing.repository.UserRepository;
import by.t1.kotor.clientprocessing.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final BlacklistRegistryRepository blacklistRegistryRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;
    private final UserMapper userMapper;

    @Override
    public ClientResponse registerClient(ClientRegistrationRequest request) {
        log.info("Registering new client: {}", request);

        if (blacklistRegistryRepository.existsBlacklistRegistriesByDocumentTypeAndDocumentId(
                request.documentType(), request.documentId())) {
            log.warn("Attempt to register blacklisted client: {}", request);
            throw new BlacklistedClientException(request.documentId());
        }

        Client client = clientMapper.toEntity(request);
        User user = userMapper.toEntity(request);
        userRepository.save(user);

        client.setUser(user);
        clientRepository.save(client);

        log.info("Client registered successfully: {}", client);
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponse findById(Long id) {
        log.info("Fetching client by id={}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        return clientMapper.toDto(client);
    }
}
