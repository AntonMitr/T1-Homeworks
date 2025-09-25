package by.t1.kotor.accountprocessing.service.impl;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.model.enums.AccountStatusEnum;
import by.t1.kotor.accountprocessing.repository.AccountRepository;
import by.t1.kotor.accountprocessing.service.AccountService;
import by.t1.kotor.common.model.dto.ClientProductMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public void createAccount(ClientProductMessage message) {
        accountRepository.findByClientIdAndProductId(message.clientId(), message.productId())
                .orElseGet(() -> {
                    Account newAccount = Account.builder()
                            .clientId(message.clientId())
                            .productId(message.productId())
                            .balance(BigDecimal.valueOf(0.0))
                            .status(AccountStatusEnum.valueOf(message.status()))
                            .cardExist(false)
                            .build();;
                    log.info("Мы сохранили{}", newAccount);
                    return accountRepository.save(newAccount);
                });
    }

}
