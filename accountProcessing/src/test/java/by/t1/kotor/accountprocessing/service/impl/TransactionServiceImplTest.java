package by.t1.kotor.accountprocessing.service.impl;

import by.t1.kotor.accountprocessing.model.*;
import by.t1.kotor.accountprocessing.model.enums.*;
import by.t1.kotor.accountprocessing.repository.*;
import by.t1.kotor.accountprocessing.service.*;
import by.t1.kotor.common.model.dto.TransactionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountService accountService;
    @Mock
    private CardService cardService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PaymentRepository paymentRepository;


    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account account;
    private Card card;
    private TransactionMessage message;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000.00"));
        account.setStatus(AccountStatusEnum.ACTIVE);

        card = new Card();
        card.setId(10L);

        message = new TransactionMessage(
                1L,
                2L,
                BigDecimal.valueOf(200),
                "CREDIT",
                LocalDateTime.now()
        );
    }

    @Test
    void create_shouldProcessCreditTransactionSuccessfully() {

        when(accountService.getAccountByClientIdAndProductId(1L, 2L)).thenReturn(account);
        when(cardService.getCardByAccountId(1L)).thenReturn(card);

        transactionService.create(message);

        verify(accountRepository, times(1)).save(argThat(a ->
                a.getBalance().equals(new BigDecimal("1200.00"))
        ));
        verify(transactionRepository, times(1)).save(argThat(tx ->
                tx.getStatus() == TransactionStatusEnum.SUCCESS &&
                        tx.getType() == TransactionTypeEnum.CREDIT
        ));
    }

    @Test
    void create_shouldFail_whenAccountBlocked() {
        account.setStatus(AccountStatusEnum.BLOCKED);

        when(accountService.getAccountByClientIdAndProductId(1L, 2L)).thenReturn(account);
        when(cardService.getCardByAccountId(1L)).thenReturn(card);

        // when
        transactionService.create(message);

        // then
        verify(transactionRepository, times(1)).save(argThat(tx ->
                tx.getStatus() == TransactionStatusEnum.FAILED
        ));
        verify(accountRepository, never()).save(any(Account.class)); // не сохраняем, т.к. заблокирован
    }

    @Test
    void create_shouldBlockAccount_whenFraudulent() {

        when(accountService.getAccountByClientIdAndProductId(1L, 2L)).thenReturn(account);
        when(cardService.getCardByAccountId(1L)).thenReturn(card);

        for (int i = 0; i < 6; i++) {
            transactionService.create(message);
        }

        verify(accountRepository, atLeastOnce()).save(argThat(a ->
                a.getStatus() == AccountStatusEnum.BLOCKED
        ));
    }

    @Test
    void autoDeductPayments_shouldDeduct_whenEnoughBalance() throws Exception {
        Payment due = Payment.builder()
                .account(account)
                .amount(new BigDecimal("200.00"))
                .paymentDate(LocalDate.now().minusDays(1))
                .build();

        when(paymentRepository.findByAccountIdAndPaymentDateBeforeAndPayedAtIsNull(
                eq(account.getId()), any(LocalDate.class))
        ).thenReturn(List.of(due));

        var method = TransactionServiceImpl.class.getDeclaredMethod("autoDeductPayments", Account.class);
        method.setAccessible(true);
        method.invoke(transactionService, account);

        assertEquals(new BigDecimal("800.00"), account.getBalance());
        assertNotNull(due.getPayedAt());
        verify(paymentRepository, times(1)).save(due);
    }

    @Test
    void autoDeductPayments_shouldMarkExpired_whenNotEnoughBalance() throws Exception {
        account.setBalance(new BigDecimal("50.00"));

        Payment due = Payment.builder()
                .account(account)
                .amount(new BigDecimal("200.00"))
                .paymentDate(LocalDate.now().minusDays(1))
                .build();

        when(paymentRepository.findByAccountIdAndPaymentDateBeforeAndPayedAtIsNull(
                eq(account.getId()), any(LocalDate.class))
        ).thenReturn(List.of(due));

        var method = TransactionServiceImpl.class.getDeclaredMethod("autoDeductPayments", Account.class);
        method.setAccessible(true);
        method.invoke(transactionService, account);

        assertTrue(due.getExpired());
        verify(paymentRepository, times(1)).save(due);
    }
}
