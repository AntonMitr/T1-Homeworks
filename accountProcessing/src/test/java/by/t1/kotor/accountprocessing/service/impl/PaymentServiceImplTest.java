package by.t1.kotor.accountprocessing.service.impl;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.model.Payment;
import by.t1.kotor.accountprocessing.model.enums.PaymentTypeEnum;
import by.t1.kotor.accountprocessing.repository.AccountRepository;
import by.t1.kotor.accountprocessing.repository.PaymentRepository;
import by.t1.kotor.common.model.dto.PaymentRegistryMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void processPayment_shouldMarkPaymentsAsPaid_whenAmountEqualsTotalDebt() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000.00"));

        Payment unpaid1 = Payment.builder().account(account).amount(new BigDecimal("100.00")).build();
        Payment unpaid2 = Payment.builder().account(account).amount(new BigDecimal("100.00")).build();
        List<Payment> unpaidPayments = List.of(unpaid1, unpaid2);

        PaymentRegistryMessage message = new PaymentRegistryMessage(
                1L,
                BigDecimal.ZERO,
                0,
                new BigDecimal("200.00"),
                LocalDate.now()
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(paymentRepository.findByAccountIdAndPayedAtIsNull(1L)).thenReturn(unpaidPayments);

        paymentService.processPayment(message);

        verify(paymentRepository, times(1)).saveAll(unpaidPayments);
        verify(paymentRepository, times(1)).save(argThat(p ->
                p.getType() == PaymentTypeEnum.FULL_REPAYMENT &&
                        p.getAmount().equals(new BigDecimal("200.00")) &&
                        p.getAccount().equals(account)
        ));
        verify(accountRepository, times(1)).save(argThat(a ->
                a.getBalance().equals(new BigDecimal("800.00"))
        ));

        assertNotNull(unpaid1.getPayedAt());
        assertNotNull(unpaid2.getPayedAt());
    }

    @Test
    void processPayment_shouldDoNothing_whenAccountNotFound() {
        PaymentRegistryMessage message = new PaymentRegistryMessage(
                99L,
                BigDecimal.ZERO,
                0,
                new BigDecimal("100.00"),
                LocalDate.now()
        );
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        paymentService.processPayment(message);

        verify(paymentRepository, never()).saveAll(any());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void processPayment_shouldWarn_whenAmountNotEqualToDebt() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000.00"));

        Payment unpaid = Payment.builder()
                .account(account)
                .amount(new BigDecimal("300.00"))
                .build();

        PaymentRegistryMessage message = new PaymentRegistryMessage(
                1L,
                BigDecimal.ZERO,
                0,
                new BigDecimal("200.00"),
                LocalDate.now()
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(paymentRepository.findByAccountIdAndPayedAtIsNull(1L)).thenReturn(List.of(unpaid));

        paymentService.processPayment(message);

        verify(paymentRepository, never()).saveAll(any());
        verify(paymentRepository, never()).save(any());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void createPaymentSchedule_shouldCreateNewPayment_whenNotExists() {
        Account account = new Account();
        account.setId(1L);

        PaymentRegistryMessage message = new PaymentRegistryMessage(
                1L,
                BigDecimal.ZERO,
                12,
                new BigDecimal("150.00"),
                LocalDate.of(2025, 11, 1)
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(paymentRepository.existsByAccountAndPaymentDate(account, message.paymentExpirationDate()))
                .thenReturn(false);

        paymentService.createPaymentSchedule(message);

        verify(paymentRepository, times(1)).save(argThat(p ->
                p.getAccount().equals(account) &&
                        p.getAmount().equals(new BigDecimal("150.00")) &&
                        p.getType() == PaymentTypeEnum.MONTHLY_INTEREST &&
                        p.getPaymentDate().equals(LocalDate.of(2025, 11, 1))
        ));
    }

    @Test
    void createPaymentSchedule_shouldNotCreate_whenAlreadyExists() {
        Account account = new Account();
        account.setId(1L);

        PaymentRegistryMessage message = new PaymentRegistryMessage(
                1L,
                BigDecimal.ZERO,
                12,
                new BigDecimal("150.00"),
                LocalDate.of(2025, 11, 1)
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(paymentRepository.existsByAccountAndPaymentDate(account, message.paymentExpirationDate()))
                .thenReturn(true);

        paymentService.createPaymentSchedule(message);

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void createPaymentSchedule_shouldThrow_whenAccountNotFound() {
        PaymentRegistryMessage message = new PaymentRegistryMessage(
                99L,
                BigDecimal.ZERO,
                12,
                new BigDecimal("100.00"),
                LocalDate.now()
        );

        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.createPaymentSchedule(message));
    }

}
