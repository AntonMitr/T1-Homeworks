package by.t1.kotor.creditprocessing.service.impl;

import by.t1.kotor.common.aop.annotation.LogDatasourceError;
import by.t1.kotor.common.model.dto.ClientProductMessage;
import by.t1.kotor.creditprocessing.mapper.ProductMapper;
import by.t1.kotor.creditprocessing.model.PaymentRegistry;
import by.t1.kotor.creditprocessing.model.ProductRegistry;
import by.t1.kotor.creditprocessing.model.dto.ProductResponse;
import by.t1.kotor.creditprocessing.repository.PaymentRegistryRepository;
import by.t1.kotor.creditprocessing.repository.ProductRegistryRepository;
import by.t1.kotor.creditprocessing.service.ProductRegistryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductRegistryServiceImpl implements ProductRegistryService {

    private final ProductRegistryRepository productRegistryRepository;
    private final PaymentRegistryRepository paymentRegistryRepository;
    private final PaymentService paymentService;
    private final ProductMapper productMapper;
    private final RestTemplate restTemplate;

    @Value("${t1.services.account-processing.url}")
    private String accountProcessingUrl;

    private Long getAccountId(ClientProductMessage message) {
        String url = accountProcessingUrl + "/api/accounts";
        try {
            ResponseEntity<Long> response = restTemplate.postForEntity(url, message, Long.class);
            Long accountId = response.getBody();
            log.debug("Created account in account-processing service with id={}", accountId);
            return accountId;
        } catch (Exception e) {
            log.error("Error creating account for clientId={}, productId={}: {}",
                    message.clientId(), message.productId(), e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ProductResponse create(ClientProductMessage message) {
        log.info("Creating ProductRegistry for clientId={}, productId={}", message.clientId(), message.productId());

        boolean exists = productRegistryRepository.existsByClientIdAndProductId(message.clientId(), message.productId());
        if (exists) {
            log.info("ProductRegistry already exists for clientId={}, productId={}", message.clientId(), message.productId());
            return null;
        }

        Long accountId = getAccountId(message);
        if (accountId == null) {
            log.error("Failed to create account, aborting ProductRegistry creation for clientId={}, productId={}",
                    message.clientId(), message.productId());
            throw new RuntimeException("Cannot create account in account-processing service");
        }

        ProductRegistry productRegistry = productMapper.toEntity(message);
        productRegistry.setAccountId(accountId);
        productRegistryRepository.save(productRegistry);
        log.info("Saved ProductRegistry with id={}", productRegistry.getId());

        List<PaymentRegistry> payments = paymentService.generateSchedule(
                productRegistry,
                message.creditAmount(),
                message.interestRate(),
                message.monthCount()
        );
        List<PaymentRegistry> savedPayments = paymentRegistryRepository.saveAll(payments);
        log.info("Saved {} PaymentRegistry entries for productRegistryId={}", savedPayments.size(), productRegistry.getId());

        ProductResponse response = productMapper.toDTO(productRegistry);
        log.debug("Finished create ProductRegistry, response={}", response);
        return response;
    }
}
