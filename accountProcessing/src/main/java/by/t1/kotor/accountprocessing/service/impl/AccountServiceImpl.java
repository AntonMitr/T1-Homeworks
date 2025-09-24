package by.t1.kotor.accountprocessing.service.impl;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.model.dto.AccountRequest;
import by.t1.kotor.accountprocessing.model.enums.AccountStatusEnum;
import by.t1.kotor.accountprocessing.repository.AccountRepository;
import by.t1.kotor.accountprocessing.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public void createAccount(AccountRequest request) {
        Account account = accountRepository
                .findByClientIdAndProductId(request.clientId(), request.productId())
                .orElseGet(() -> {
                    Account newAccount = Account.builder()
                            .clientId(request.clientId())
                            .productId(request.productId())
                            .balance(BigDecimal.valueOf(0.0))
                            .status(AccountStatusEnum.valueOf(request.status()))
                            .cardExist(false)
                            .build();
                    return accountRepository.save(newAccount);
                });
    }

}
