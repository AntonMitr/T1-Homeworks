package by.t1.kotor.accountprocessing.service.impl;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.model.Card;
import by.t1.kotor.accountprocessing.model.enums.AccountStatusEnum;
import by.t1.kotor.accountprocessing.repository.AccountRepository;
import by.t1.kotor.accountprocessing.repository.CardRepository;
import by.t1.kotor.accountprocessing.service.AccountService;
import by.t1.kotor.common.model.dto.CardMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    private Account activeAccount;

    @BeforeEach
    void setUp() {
        activeAccount = Account.builder()
                .clientId(10L)
                .productId(20L)
                .balance(BigDecimal.ZERO)
                .status(AccountStatusEnum.ACTIVE)
                .cardExist(false)
                .build();
        activeAccount.setId(1L);

        cardService = new CardServiceImpl(cardRepository, accountService, restTemplate, accountRepository);
    }
}
