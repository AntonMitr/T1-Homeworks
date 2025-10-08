package by.t1.kotor.accountprocessing.service.impl;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.model.Payment;
import by.t1.kotor.accountprocessing.model.enums.PaymentTypeEnum;
import by.t1.kotor.accountprocessing.repository.AccountRepository;
import by.t1.kotor.accountprocessing.repository.PaymentRepository;
import by.t1.kotor.accountprocessing.service.PaymentService;
import by.t1.kotor.common.model.dto.PaymentRegistryMessage;
import by.t1.kotor.crosscuttingstarter.aop.annotation.LogDatasourceError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    @LogDatasourceError
    public void processPayment(PaymentRegistryMessage message) {
        log.debug("Processing payment message: {}", message);

        Account account = accountRepository.findById(message.accountId()).orElse(null);
        if (account == null) {
            log.warn("Account not found for accountId={}", message.accountId());
            return;
        }
        log.debug("Account found: id={}, balance={}", account.getId(), account.getBalance());

        BigDecimal totalDebt = paymentRepository.findByAccountIdAndPayedAtIsNull(account.getId())
                .stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.debug("Total unpaid debt for account {}: {}", account.getId(), totalDebt);

        if (message.amount().compareTo(totalDebt) == 0) {
            List<Payment> unpaidPayments = paymentRepository.findByAccountIdAndPayedAtIsNull(account.getId());
            unpaidPayments.forEach(p -> p.setPayedAt(LocalDateTime.now()));
            paymentRepository.saveAll(unpaidPayments);
            log.info("All unpaid payments marked as paid for account {}", account.getId());

            Payment newPayment = Payment.builder()
                    .account(account)
                    .amount(message.amount())
                    .paymentDate(LocalDate.now())
                    .payedAt(LocalDateTime.now())
                    .isCredit(true)
                    .expired(false)
                    .type(PaymentTypeEnum.FULL_REPAYMENT)
                    .build();
            paymentRepository.save(newPayment);
            log.info("New payment record created: accountId={}, amount={}", account.getId(), message.amount());

            account.setBalance(account.getBalance().subtract(message.amount()));
            accountRepository.save(account);
            log.info("Account balance updated after payment: accountId={}, newBalance={}", account.getId(), account.getBalance());
        } else {
            log.warn("Payment amount {} does not match debt {} for account {}", message.amount(), totalDebt, account.getId());
        }
    }

    @LogDatasourceError
    public void createPaymentSchedule(PaymentRegistryMessage message) {
        Account account = accountRepository.findById(message.accountId())
                .orElseThrow(() -> new RuntimeException("Account not found: " + message.accountId()));

        log.info("Received partial payment for account {}: amount={}", account.getId(), message.amount());

        boolean exists = paymentRepository.existsByAccountAndPaymentDate(account, message.paymentExpirationDate());
        if (exists) {
            log.info("Payment already exists for accountId={} on {}", account.getId(), message.paymentExpirationDate());
            return;
        }

        Payment payment = new Payment();
        payment.setAccount(account);
        payment.setPaymentDate(message.paymentExpirationDate());
        payment.setAmount(message.amount());
        payment.setIsCredit(true);
        payment.setExpired(false);
        payment.setType(PaymentTypeEnum.MONTHLY_INTEREST);
        payment.setPayedAt(null);
        paymentRepository.save(payment);

        log.info("Scheduled new payment for account {} on {}: amount={}", account.getId(), message.paymentExpirationDate(), message.amount());
    }
}
