package by.t1.kotor.accountprocessing.service;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.common.model.dto.ClientProductMessage;

public interface AccountService {
    Account createAccount(ClientProductMessage request);

    Account getAccountByClientIdAndProductId(Long clientId, Long productId);
}
