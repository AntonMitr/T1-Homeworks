package by.t1.kotor.creditprocessing.service.impl;

import by.t1.kotor.common.aop.annotation.LogDatasourceError;
import by.t1.kotor.creditprocessing.model.PaymentRegistry;
import by.t1.kotor.creditprocessing.model.ProductRegistry;
import by.t1.kotor.creditprocessing.repository.ProductRegistryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditService {

    private final ProductRegistryRepository productRegistryRepository;

    public BigDecimal calculateCurrentDebt(Long clientId) {
        log.debug("Start calculateCurrentDebt for clientId={}", clientId);

        List<ProductRegistry> registries = productRegistryRepository.findByClientId(clientId);
        BigDecimal totalDebt = registries.stream()
                .flatMap(pr -> pr.getPaymentRegistryList().stream())
                .filter(p -> Boolean.FALSE.equals(p.getExpired()) && p.getDebtAmount() != null)
                .map(PaymentRegistry::getDebtAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.debug("Total current debt for clientId={} is {}", clientId, totalDebt);
        return totalDebt;
    }

    public boolean hasExpired(Long clientId) {
        log.debug("Start hasExpired check for clientId={}", clientId);

        List<ProductRegistry> registries = productRegistryRepository.findByClientId(clientId);
        boolean expired = registries.stream()
                .flatMap(pr -> pr.getPaymentRegistryList().stream())
                .anyMatch(PaymentRegistry::getExpired);

        log.debug("Has expired payments for clientId={} : {}", clientId, expired);
        return expired;
    }
}
