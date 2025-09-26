package by.t1.kotor.creditprocessing.service.impl;

import by.t1.kotor.common.model.dto.ClientProductMessage;
import by.t1.kotor.common.model.dto.CreditDecisionMessage;
import by.t1.kotor.creditprocessing.kafka.KafkaProducer;
import by.t1.kotor.creditprocessing.mapper.DecisionMapper;
import by.t1.kotor.creditprocessing.model.Reason;
import by.t1.kotor.creditprocessing.model.dto.ClientInfo;
import by.t1.kotor.creditprocessing.service.ClientInfoService;
import by.t1.kotor.creditprocessing.service.ProductRegistryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditDecisionService {

    private final CreditService creditService;
    private final ProductRegistryService productRegistryService;
    private final ClientInfoService clientInfoService;
    private final DecisionMapper decisionMapper;
    private final KafkaProducer<CreditDecisionMessage> kafkaProducer;

    @Value("${t1.credit.max-total-debt}")
    private BigDecimal limitTotalDebt;
    @Value("${t1.kafka.topic.credit_decisions}")
    private String creditDecisionTopic;

    public void decideCredit(ClientProductMessage message) {
        log.debug("Start decideCredit for clientId={}, productId={}", message.clientId(), message.productId());

        ClientInfo clientInfo = clientInfoService.getClientInfo(message.clientId());
        BigDecimal currentDebt = creditService.calculateCurrentDebt(message.clientId());
        BigDecimal totalDebt = currentDebt.add(message.creditAmount());
        boolean hasExpired = creditService.hasExpired(message.clientId());

        log.info("Client {} {} {}, currentDebt={}, totalDebt={}, hasExpired={}",
                clientInfo.firstName(), clientInfo.middleName(), clientInfo.lastName(),
                currentDebt, totalDebt, hasExpired);

        CreditDecisionMessage decisionMessage;

        if (totalDebt.compareTo(limitTotalDebt) > 0) {
            decisionMessage = decisionMapper.toMessage(message, false, Reason.OVER_LIMIT);
            log.info("Credit denied: over limit for clientId={}", message.clientId());
        } else if (hasExpired) {
            decisionMessage = decisionMapper.toMessage(message, false, Reason.HAS_EXPIRED);
            log.info("Credit denied: has expired for clientId={}", message.clientId());
        } else {
            productRegistryService.create(message);
            decisionMessage = decisionMapper.toMessage(message, true, null);
            log.info("Credit approved for clientId={}", message.clientId());
        }

        kafkaProducer.sendTo(creditDecisionTopic, decisionMessage);
        log.debug("End decideCredit for clientId={}, decision={}", message.clientId(), decisionMessage);
    }
}
