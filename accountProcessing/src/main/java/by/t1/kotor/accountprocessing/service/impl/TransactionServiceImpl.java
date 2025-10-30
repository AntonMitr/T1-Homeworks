package by.t1.kotor.accountprocessing.service.impl;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.model.Card;
import by.t1.kotor.accountprocessing.model.Payment;
import by.t1.kotor.accountprocessing.model.Transaction;
import by.t1.kotor.accountprocessing.model.enums.AccountStatusEnum;
import by.t1.kotor.accountprocessing.model.enums.TransactionStatusEnum;
import by.t1.kotor.accountprocessing.model.enums.TransactionTypeEnum;
import by.t1.kotor.accountprocessing.repository.AccountRepository;
import by.t1.kotor.accountprocessing.repository.PaymentRepository;
import by.t1.kotor.accountprocessing.repository.TransactionRepository;
import by.t1.kotor.accountprocessing.service.AccountService;
import by.t1.kotor.accountprocessing.service.CardService;
import by.t1.kotor.accountprocessing.service.TransactionService;
import by.t1.kotor.common.model.dto.TransactionMessage;
import by.t1.kotor.crosscuttingstarter.aop.annotation.LogDatasourceError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final AccountService accountService;
    private final CardService cardService;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    private final Map<Long, List<LocalDateTime>> cardTransactions = new ConcurrentHashMap<>();
    private final int N = 5;
    private final Duration T = Duration.ofMinutes(1);

    @Override
    @LogDatasourceError
    public void create(TransactionMessage msg) {
        log.info("Received transaction: clientId={}, productId={}, type={}, amount={}",
                msg.clientId(), msg.productId(), msg.transactionType(), msg.amount());

        Account account = accountService.getAccountByClientIdAndProductId(msg.clientId(), msg.productId());
        Card card = cardService.getCardByAccountId(account.getId());

        log.debug("Account details: id={}, balance={}, status={}", account.getId(), account.getBalance(), account.getStatus());

        if (account.getStatus() == AccountStatusEnum.BLOCKED || account.getStatus() == AccountStatusEnum.CLOSED) {
            saveTransaction(msg, TransactionStatusEnum.FAILED, account, card);
            log.warn("Transaction rejected: account {} status={}", account.getId(), account.getStatus());
            return;
        }

        if (isFraudulent(card.getId())) {
            account.setStatus(AccountStatusEnum.BLOCKED);
            accountRepository.save(account);
            saveTransaction(msg, TransactionStatusEnum.FAILED, account, card);
            log.warn("Account {} blocked due to transaction limit exceeded for card {}", account.getId(), card.getId());
            return;
        }

        if ("DEBIT".equals(msg.transactionType())) {
            log.debug("Before debit: accountId={}, balance={}", account.getId(), account.getBalance());
            account.setBalance(account.getBalance().subtract(msg.amount()));
            log.info("Debited {} from account {}. New balance={}", msg.amount(), account.getId(), account.getBalance());
        } else if ("CREDIT".equals(msg.transactionType())) {
            log.debug("Before credit: accountId={}, balance={}", account.getId(), account.getBalance());
            account.setBalance(account.getBalance().add(msg.amount()));
            log.info("Credited {} to account {}. New balance={}", msg.amount(), account.getId(), account.getBalance());
        }

        accountRepository.save(account);
        saveTransaction(msg, TransactionStatusEnum.SUCCESS, account, card);
        log.debug("Transaction saved in DB: accountId={}, cardId={}", account.getId(), card.getId());

        autoDeductPayments(account);
        log.info("Auto-deduction check completed for account {}", account.getId());
    }

    private void saveTransaction(TransactionMessage message, TransactionStatusEnum status, Account account, Card card) {
        Transaction tx = Transaction.builder()
                .account(account)
                .card(card)
                .amount(message.amount())
                .type(TransactionTypeEnum.valueOf(message.transactionType()))
                .status(status)
                .timestamp(message.timeStamp())
                .build();
        transactionRepository.save(tx);
        log.debug("Transaction record saved: accountId={}, cardId={}, status={}", account.getId(), card.getId(), status);
    }

    private boolean isFraudulent(Long cardId) {
        LocalDateTime now = LocalDateTime.now();
        cardTransactions.putIfAbsent(cardId, new ArrayList<>());
        List<LocalDateTime> txns = cardTransactions.get(cardId);

        txns.removeIf(t -> t.isBefore(now.minus(T)));
        txns.add(now);

        log.debug("Fraud check: cardId={}, transactionsInLastMinute={}", cardId, txns.size());
        return txns.size() > N;
    }

    private void autoDeductPayments(Account account) {
        LocalDate today = LocalDate.now();
        List<Payment> duePayments = paymentRepository.findByAccountIdAndPaymentDateBeforeAndPayedAtIsNull(account.getId(), today);

        for (Payment p : duePayments) {
            if (account.getBalance().compareTo(p.getAmount()) >= 0) {
                log.info("Auto-deducting payment: accountId={}, amount={}", account.getId(), p.getAmount());
                account.setBalance(account.getBalance().subtract(p.getAmount()));
                p.setPayedAt(LocalDateTime.now());
            } else {
                log.warn("Insufficient balance for auto-deduction: accountId={}, paymentAmount={}", account.getId(), p.getAmount());
                p.setExpired(true);
            }
            paymentRepository.save(p);
            accountRepository.save(account);
        }
    }
}
