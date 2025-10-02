package by.t1.kotor.accountprocessing.service.impl;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.model.Card;
import by.t1.kotor.accountprocessing.model.enums.AccountStatusEnum;
import by.t1.kotor.accountprocessing.model.enums.CardStatusEnum;
import by.t1.kotor.accountprocessing.model.enums.PaymentSystemEnum;
import by.t1.kotor.accountprocessing.repository.AccountRepository;
import by.t1.kotor.accountprocessing.repository.CardRepository;
import by.t1.kotor.accountprocessing.service.AccountService;
import by.t1.kotor.accountprocessing.service.CardService;
import by.t1.kotor.common.aop.annotation.LogDatasourceError;
import by.t1.kotor.common.model.dto.CardMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountService accountService;
    private final RestTemplate restTemplate;
    private final AccountRepository accountRepository;

    @Value("${t1.kafka.services.client-processing.url}")
    private String baseUrl;

    @LogDatasourceError
    public void create(CardMessage message) {
        log.debug("Received message to create card: clientId={}, productId={}, cardType={}",
                message.clientId(), message.productId(), message.cardType());

        Account account = accountService.getAccountByClientIdAndProductId(message.clientId(), message.productId());

        if (account.getStatus() != AccountStatusEnum.ACTIVE) {
            log.info("Account {} is not active (status={}), card not created", account.getId(), account.getStatus());
            return;
        }

        String productKey = getProductKey(message.productId());
        log.debug("Product key for productId {}: {}", message.productId(), productKey);

        if (!Set.of("DC", "CC").contains(productKey)) {
            log.info("ProductKey {} not supported for account {}, card not created", productKey, account.getId());
            return;
        }

        if (Boolean.TRUE.equals(account.getCardExist())) {
            log.info("Card already exists for account {}. Skipping creation", account.getId());
            return;
        }

        Card card = Card.builder()
                .account(account)
                .cardId(UUID.randomUUID().toString())
                .paymentSystem(PaymentSystemEnum.valueOf(message.cardType()))
                .status(CardStatusEnum.ACTIVE)
                .build();
        cardRepository.save(card);
        log.info("Created card {} for account {}", card.getCardId(), account.getId());

        account.setCardExist(true);
        accountRepository.save(account);
        log.debug("Updated account {}: set cardExist=true", account.getId());
    }

    public Card getCardByAccountId(Long accountId) {
        return cardRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> {
                    log.warn("Card not found for accountId={}", accountId);
                    return new IllegalArgumentException("Card not found");
                });
    }

    private String getProductKey(Long productId) {
        try {
            Map<String, Object> response = restTemplate.getForObject(
                    baseUrl + "/api/products/" + productId, Map.class);
            String key = response != null ? (String) response.get("key") : null;
            if (key == null) {
                log.warn("Product key is null for productId {}", productId);
            }
            return key;
        } catch (Exception ex) {
            log.error("Error fetching product key for productId {}: {}", productId, ex.getMessage());
            return null;
        }
    }
}
