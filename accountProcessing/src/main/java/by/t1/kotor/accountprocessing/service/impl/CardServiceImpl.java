package by.t1.kotor.accountprocessing.service.impl;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.model.Card;
import by.t1.kotor.accountprocessing.model.dto.CardRequest;
import by.t1.kotor.accountprocessing.model.enums.AccountStatusEnum;
import by.t1.kotor.accountprocessing.model.enums.CardStatusEnum;
import by.t1.kotor.accountprocessing.model.enums.PaymentSystemEnum;
import by.t1.kotor.accountprocessing.repository.AccountRepository;
import by.t1.kotor.accountprocessing.repository.CardRepository;
import by.t1.kotor.accountprocessing.service.CardService;
import by.t1.kotor.common.model.dto.CardMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public void create(CardMessage message) {
        Account account = accountRepository
                .findByClientIdAndProductId(message.clientId(), message.productId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getStatus() != AccountStatusEnum.ACTIVE) {
            log.info("Account {} is not active, card not created", account.getId());
            return;
        }
        if (!Set.of("DC", "CC").contains(message.productKey())) {
            log.info("ProductKey {} not supported, card not created", message.productKey());
            return;
        }
        if (Boolean.TRUE.equals(account.getCardExist())) {
            log.info("Card already exists for account {}, skipping creation", account.getId());
            return;
        }
        Card card = Card.builder()
                .account(account)
                .cardId(UUID.randomUUID().toString())
                .paymentSystem(PaymentSystemEnum.valueOf(message.cardType()))
                .status(CardStatusEnum.ACTIVE)
                .build();
        cardRepository.save(card);

        account.setCardExist(true);
        accountRepository.save(account);
    }
}
