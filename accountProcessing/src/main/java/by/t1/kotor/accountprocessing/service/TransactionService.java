package by.t1.kotor.accountprocessing.service;

import by.t1.kotor.common.model.dto.TransactionMessage;

public interface TransactionService {
    void create(TransactionMessage message);
}
