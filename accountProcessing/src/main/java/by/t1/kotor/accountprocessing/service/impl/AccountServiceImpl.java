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

    public Account createAccount(ClientProductMessage message) {
        log.debug("Received message to create account: clientId={}, productId={}",
                message.clientId(), message.productId());

        return accountRepository.findByClientIdAndProductId(message.clientId(), message.productId())
                .orElseGet(() -> {
                    Account newAccount = Account.builder()
                            .clientId(message.clientId())
                            .productId(message.productId())
                            .balance(BigDecimal.ZERO)
                            .interestRate(message.interestRate())
                            .isRecalc(false)
                            .status(AccountStatusEnum.ACTIVE)
                            .cardExist(false)
                            .build();

                    if (newAccount.getInterestRate() != null) {
                        newAccount.setIsRecalc(true);
                    }

                    Account savedAccount = accountRepository.save(newAccount);
                    log.info("Created new account: id={}, clientId={}, productId={}, status={}",
                            savedAccount.getId(), savedAccount.getClientId(),
                            savedAccount.getProductId(), savedAccount.getStatus());
                    return savedAccount;
                });
    }

    public Account getAccountByClientIdAndProductId(Long clientId, Long productId) {
        return accountRepository
                .findByClientIdAndProductId(clientId, productId)
                .orElseThrow(() -> {
                    log.warn("Account not found for clientId={} and productId={}",
                            clientId, productId);
                    return new IllegalArgumentException("Account not found");
                });
    }
}
