package by.t1.kotor.creditprocessing.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductRegistryServiceImpl implements ProductRegistryService {

    private final ProductRegistryRepository productRegistryRepository;
    private final PaymentRegistryRepository paymentRegistryRepository;
    private final PaymentScheduleService paymentScheduleService;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse create(ClientProductMessage message) {
        log.debug("Start create ProductRegistry for clientId={}, productId={}", message.clientId(), message.productId());

        ProductRegistry productRegistry = productMapper.toEntity(message);
        productRegistry.setAccountId(6L);
        productRegistryRepository.save(productRegistry);
        log.info("Saved ProductRegistry with id={}", productRegistry.getId());

        List<PaymentRegistry> payments = paymentScheduleService.generateSchedule(
                productRegistry,
                message.creditAmount(),
                message.interestRate(),
                message.monthCount()
        );
        List<PaymentRegistry> savedPayments = paymentRegistryRepository.saveAll(payments);
        log.info("Saved {} PaymentRegistry entries for productRegistryId={}", savedPayments.size(), productRegistry.getId());

        ProductResponse response = productMapper.toDTO(productRegistry);
        log.debug("End create ProductRegistry, response={}", response);
        return response;
    }
}
