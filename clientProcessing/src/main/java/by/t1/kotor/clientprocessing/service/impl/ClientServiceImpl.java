package by.t1.kotor.clientprocessing.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

        if (blacklistRegistryRepository.existsBlacklistRegistriesByDocumentTypeAndDocumentId(
                request.documentType(), request.documentId())) {
            throw new IllegalStateException("The client is blacklisted");
        }

        Client client = clientMapper.toEntity(request);
        User user = userMapper.toEntity(request);
        userRepository.save(user);

        client.setUser(user);
        clientRepository.save(client);

        return clientMapper.toDto(client);
    }
}
