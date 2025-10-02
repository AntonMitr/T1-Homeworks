package by.t1.kotor.creditprocessing.service;

import by.t1.kotor.common.aop.annotation.LogDatasourceError;
import by.t1.kotor.creditprocessing.model.dto.ClientInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientInfoService {
    private final RestTemplate restTemplate;
    @Value("${t1.services.client-processing.url}")
    private String clientProcessingUrl;

    @LogDatasourceError
    public ClientInfo getClientInfo(Long id) {
        log.debug("Start getClientInfo, id={}", id);
        String url = clientProcessingUrl + "/api/clients/" + id;
        try {
            ClientInfo clientInfo = restTemplate.getForObject(url, ClientInfo.class);
            if (clientInfo == null) {
                log.warn("Client with id {} not found (null response)", id);
                throw new IllegalStateException("Client not found");
            }
            log.debug("Successfully retrieved ClientInfo: {}", clientInfo);
            return clientInfo;
        } catch (RestClientException e) {
            log.error("Failed to get client info for id {}: {}", id, e.getMessage(), e);
            throw new IllegalStateException("Cannot retrieve client info", e);
        } finally {
            log.debug("End getClientInfo, id={}", id);
        }
    }
}
