package by.t1.kotor.accountprocessing.service.impl;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.model.enums.AccountStatusEnum;
import by.t1.kotor.accountprocessing.repository.AccountRepository;
import by.t1.kotor.common.model.dto.ClientProductMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl service;

    private ClientProductMessage message;
    private Account existingAccount;
    private Account newAccount;
    private Account savedAccount;

    @BeforeEach
    void setUp() {
        message = ClientProductMessage.builder()
                .clientId(1L)
                .productId(2L)
                .interestRate(BigDecimal.valueOf(5))
                .build();

        existingAccount = Account.builder()
                .clientId(1L)
                .productId(2L)
                .balance(BigDecimal.ZERO)
                .status(AccountStatusEnum.ACTIVE)
                .build();

        newAccount = Account.builder()
                .clientId(1L)
                .productId(2L)
                .interestRate(BigDecimal.valueOf(5))
                .balance(BigDecimal.ZERO)
                .status(AccountStatusEnum.ACTIVE)
                .isRecalc(false)
                .cardExist(false)
                .build();

        savedAccount = Account.builder()
                .clientId(1L)
                .productId(2L)
                .interestRate(BigDecimal.valueOf(5))
                .balance(BigDecimal.ZERO)
                .status(AccountStatusEnum.ACTIVE)
                .isRecalc(false)
                .cardExist(false)
                .build();

        savedAccount.setId(100L);
    }

    @Test
    void createAccount_shouldReturnExistingAccount_whenAlreadyExists() {
        when(accountRepository.findByClientIdAndProductId(1L, 2L))
                .thenReturn(Optional.of(existingAccount));

        Account result = service.createAccount(message);

        assertEquals(existingAccount, result);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void createAccount_shouldCreateNewAccount_whenNotExists() {
        when(accountRepository.findByClientIdAndProductId(1L, 2L))
                .thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class)))
                .thenReturn(savedAccount);

        Account result = service.createAccount(message);

        assertNotNull(result);
        assertEquals(savedAccount.getId(), result.getId());
        assertEquals(AccountStatusEnum.ACTIVE, result.getStatus());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void getAccountByClientIdAndProductId_shouldReturnAccount_whenFound() {
        when(accountRepository.findByClientIdAndProductId(1L, 2L))
                .thenReturn(Optional.of(existingAccount));

        Account result = service.getAccountByClientIdAndProductId(1L, 2L);

        assertEquals(existingAccount, result);
    }

    @Test
    void getAccountByClientIdAndProductId_shouldThrowException_whenNotFound() {
        when(accountRepository.findByClientIdAndProductId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.getAccountByClientIdAndProductId(1L, 2L));
    }
}
