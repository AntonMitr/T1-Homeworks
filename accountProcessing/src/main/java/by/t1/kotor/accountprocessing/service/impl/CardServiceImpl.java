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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public void create(CardRequest request) {
        Account account = accountRepository
                .findByClientIdAndProductId(request.clientId(), request.productId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getStatus() == AccountStatusEnum.ACTIVE) {
            if (request.productKey().equals("DC") || request.productKey().equals("CC")) {
                if (!Boolean.TRUE.equals(account.getCardExist())) {
                    Card card = Card.builder()
                            .account(account)
                            .cardId(UUID.randomUUID().toString())
                            .PaymentSystem(PaymentSystemEnum.valueOf(request.cardType()))
                            .status(CardStatusEnum.ACTIVE)
                            .build();
                    cardRepository.save(card);

                    account.setCardExist(true);
                    accountRepository.save(account);
                }
            }
        }
    }
}
