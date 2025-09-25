package by.t1.kotor.creditprocessing.service;

import by.t1.kotor.creditprocessing.model.dto.ClientInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ClientInfoService {
    private final RestTemplate restTemplate;

    public ClientInfo getClientInfo(String clientId) {
        String url = "http://ms-1/clients/" + clientId;
        return restTemplate.getForObject(url, ClientInfo.class);
    }

}
