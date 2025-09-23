package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.mapper.ClientMapper;
import by.t1.kotor.clientprocessing.model.BlacklistRegistry;
import by.t1.kotor.clientprocessing.model.Client;
import by.t1.kotor.clientprocessing.model.User;
import by.t1.kotor.clientprocessing.model.dto.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.ClientResponse;
import by.t1.kotor.clientprocessing.repository.BlacklistRegistryRepository;
import by.t1.kotor.clientprocessing.repository.ClientRepository;
import by.t1.kotor.clientprocessing.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final BlacklistRegistryRepository blacklistRegistryRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponse registerClient(ClientRegistrationRequest request) {

        if (blacklistRegistryRepository.existsBlacklistRegistriesByDocumentTypeAndDocumentId(
                request.documentType(), request.documentId())) {
            throw new IllegalStateException("Клиент находится в черном списке");
        }

        Client client = clientMapper.toEntity(request);

        User user = User.builder()
                .login(request.login())
                .email(request.email())
                .password(request.password())
                .build();

        client.setUser(user);
        clientRepository.save(client);

        return clientMapper.toDto(client);
    }
}
