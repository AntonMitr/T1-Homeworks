package by.t1.kotor.accountprocessing.service;

import by.t1.kotor.common.model.dto.PaymentRegistryMessage;

public interface PaymentService {
    void createPaymentSchedule(PaymentRegistryMessage message);

    void processPayment(PaymentRegistryMessage message);
}
