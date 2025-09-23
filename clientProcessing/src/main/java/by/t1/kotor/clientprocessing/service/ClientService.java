package by.t1.kotor.clientprocessing.service;

import by.t1.kotor.clientprocessing.model.dto.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.ClientResponse;

public interface ClientService {
    ClientResponse registerClient(ClientRegistrationRequest client);
}
