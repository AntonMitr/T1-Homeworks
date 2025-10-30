package by.t1.kotor.creditprocessing.service.impl;

import by.t1.kotor.creditprocessing.model.PaymentRegistry;
import by.t1.kotor.creditprocessing.model.ProductRegistry;
import by.t1.kotor.creditprocessing.repository.ProductRegistryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private ProductRegistryRepository productRegistryRepository;

    @InjectMocks
    private CreditService creditService;

    @Test
    void calculateCurrentDebt_shouldSumOnlyNonExpiredDebts() {
        Long clientId = 1L;

        PaymentRegistry p1 = new PaymentRegistry();
        p1.setDebtAmount(BigDecimal.valueOf(1000));
        p1.setExpired(false);

        PaymentRegistry p2 = new PaymentRegistry();
        p2.setDebtAmount(BigDecimal.valueOf(500));
        p2.setExpired(true);

        ProductRegistry pr = new ProductRegistry();
        pr.setPaymentRegistryList(List.of(p1, p2));

        when(productRegistryRepository.findByClientId(clientId)).thenReturn(List.of(pr));

        BigDecimal totalDebt = creditService.calculateCurrentDebt(clientId);
        assertEquals(BigDecimal.valueOf(1000), totalDebt);
    }

    @Test
    void hasExpired_shouldReturnTrueIfAnyPaymentExpired() {
        Long clientId = 1L;

        PaymentRegistry p1 = new PaymentRegistry();
        p1.setExpired(false);

        PaymentRegistry p2 = new PaymentRegistry();
        p2.setExpired(true);

        ProductRegistry pr = new ProductRegistry();
        pr.setPaymentRegistryList(List.of(p1, p2));

        when(productRegistryRepository.findByClientId(clientId)).thenReturn(List.of(pr));

        assertTrue(creditService.hasExpired(clientId));
    }

    @Test
    void hasExpired_shouldReturnFalseIfNoPaymentsExpired() {
        Long clientId = 1L;

        PaymentRegistry p1 = new PaymentRegistry();
        p1.setExpired(false);

        ProductRegistry pr = new ProductRegistry();
        pr.setPaymentRegistryList(List.of(p1));

        when(productRegistryRepository.findByClientId(clientId)).thenReturn(List.of(pr));

        assertFalse(creditService.hasExpired(clientId));
    }
}
