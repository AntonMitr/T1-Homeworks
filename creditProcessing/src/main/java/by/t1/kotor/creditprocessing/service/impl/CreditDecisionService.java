package by.t1.kotor.creditprocessing.service.impl;

import by.t1.kotor.common.model.dto.ClientProductMessage;
import by.t1.kotor.creditprocessing.kafka.KafkaProducer;
import by.t1.kotor.creditprocessing.model.dto.ClientInfo;
import by.t1.kotor.creditprocessing.service.ClientInfoService;
import by.t1.kotor.creditprocessing.service.ProductRegistryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditDecisionService {

    private final CreditService creditService;
    private final ProductRegistryService productRegistryService;
    private final ClientInfoService clientInfoService;

    @Value("${t1.credit.max-total-debt}")
    private BigDecimal limitTotalDebt;


    public void decideCredit(ClientProductMessage message) {
        log.debug("Start decideCredit for clientId={}, productId={}", message.clientId(), message.productId());

        ClientInfo clientInfo = clientInfoService.getClientInfo(message.clientId());
        BigDecimal currentDebt = creditService.calculateCurrentDebt(message.clientId());
        BigDecimal totalDebt = currentDebt.add(message.creditAmount());
        boolean hasExpired = creditService.hasExpired(message.clientId());

        log.info("Client {} {} {}, currentDebt={}, totalDebt={}, hasExpired={}",
                clientInfo.firstName(), clientInfo.middleName(), clientInfo.lastName(),
                currentDebt, totalDebt, hasExpired);

        if (totalDebt.compareTo(limitTotalDebt) > 0) {
            log.info("Credit denied: over limit for clientId={}", message.clientId());
        } else if (hasExpired) {
            log.info("Credit denied: has expired for clientId={}", message.clientId());
        } else {
            productRegistryService.create(message);
            log.info("Credit approved for clientId={}", message.clientId());
        }
    }

}
