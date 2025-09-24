package by.t1.kotor.clientprocessing.service;

import by.t1.kotor.clientprocessing.model.dto.client.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.client.ClientResponse;

public interface ClientService {
    ClientResponse registerClient(ClientRegistrationRequest client);
}
