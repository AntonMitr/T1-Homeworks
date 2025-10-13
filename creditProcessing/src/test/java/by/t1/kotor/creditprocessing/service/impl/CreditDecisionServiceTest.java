package by.t1.kotor.creditprocessing.service.impl;

import by.t1.kotor.common.model.dto.ClientProductMessage;
import by.t1.kotor.creditprocessing.model.dto.ClientInfo;
import by.t1.kotor.creditprocessing.service.ClientInfoService;
import by.t1.kotor.creditprocessing.service.ProductRegistryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditDecisionServiceTest {

    @Mock
    private CreditService creditService;

    @Mock
    private ProductRegistryService productRegistryService;

    @Mock
    private ClientInfoService clientInfoService;

    private ClientProductMessage message;
    private ClientInfo clientInfo;

    @InjectMocks
    private CreditDecisionService creditDecisionService;

    @BeforeEach
    void setUp() {
        org.springframework.test.util.ReflectionTestUtils.setField(creditDecisionService,
                "limitTotalDebt", BigDecimal.valueOf(10000));

        message = ClientProductMessage.builder()
                .clientId(1L)
                .productId(1L)
                .creditAmount(BigDecimal.valueOf(1000))
                .interestRate(BigDecimal.valueOf(5))
                .monthCount(12)
                .build();

        clientInfo = ClientInfo.builder()
                .firstName("Ann")
                .middleName("Test")
                .lastName("Valida")
                .documentId("123123")
                .build();
    }

    @Test
    void decideCredit_shouldApprove_whenDebtWithinLimitAndNoExpiredPayments() {

        when(clientInfoService.getClientInfo(1L)).thenReturn(clientInfo);
        when(creditService.calculateCurrentDebt(1L)).thenReturn(BigDecimal.valueOf(1000));
        when(creditService.hasExpired(1L)).thenReturn(false);

        creditDecisionService.decideCredit(message);

        verify(productRegistryService, times(1)).create(any(ClientProductMessage.class));
    }

    @Test
    void decideCredit_shouldDeny_whenDebtExceedsLimit() {

        when(clientInfoService.getClientInfo(1L)).thenReturn(clientInfo);
        when(creditService.calculateCurrentDebt(1L)).thenReturn(BigDecimal.valueOf(20000));
        when(creditService.hasExpired(1L)).thenReturn(false);

        creditDecisionService.decideCredit(message);

        verify(productRegistryService, never()).create(any());
    }

    @Test
    void decideCredit_shouldDeny_whenClientHasExpiredPayments() {

        when(clientInfoService.getClientInfo(1L)).thenReturn(clientInfo);
        when(creditService.calculateCurrentDebt(1L)).thenReturn(BigDecimal.valueOf(3000));
        when(creditService.hasExpired(1L)).thenReturn(true);

        creditDecisionService.decideCredit(message);

        verify(productRegistryService, never()).create(any());
    }

}
