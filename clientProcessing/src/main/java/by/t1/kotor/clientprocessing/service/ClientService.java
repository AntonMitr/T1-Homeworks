package by.t1.kotor.clientprocessing.service;

import by.t1.kotor.clientprocessing.model.Role;
import by.t1.kotor.clientprocessing.model.dto.client.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.client.ClientResponse;

import java.util.Set;

public interface ClientService {
    ClientResponse registerClient(ClientRegistrationRequest client, Set<Role> roles);

    ClientResponse findById(Long id);

    void blockClient(Long clientId);
}
