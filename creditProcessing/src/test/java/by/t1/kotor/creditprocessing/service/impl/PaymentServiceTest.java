package by.t1.kotor.creditprocessing.service.impl;

import by.t1.kotor.common.model.dto.PaymentRegistryMessage;
import by.t1.kotor.creditprocessing.kafka.KafkaProducer;
import by.t1.kotor.creditprocessing.mapper.PaymentMapper;
import by.t1.kotor.creditprocessing.model.PaymentRegistry;
import by.t1.kotor.creditprocessing.model.ProductRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private KafkaProducer<PaymentRegistryMessage> kafkaProducer;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void testGenerateSchedule() {
        ProductRegistry product = ProductRegistry.builder()
                .clientId(1L)
                .accountId(1L)
                .productId(1L)
                .openDate(LocalDate.of(2025, 10, 1))
                .build();

        BigDecimal creditAmount = new BigDecimal("1000");
        BigDecimal interestRate = new BigDecimal("12");
        int monthCount = 3;

        PaymentRegistryMessage dummyMessage = new PaymentRegistryMessage();
        when(paymentMapper.toMessage(any(PaymentRegistry.class))).thenReturn(dummyMessage);

        List<PaymentRegistry> schedule = paymentService.generateSchedule(product, creditAmount, interestRate, monthCount);

        assertEquals(3, schedule.size(), "Should generate 3 payments");

        PaymentRegistry firstPayment = schedule.getFirst();
        assertNotNull(firstPayment.getPaymentDate(), "Payment date should not be null");
        assertEquals(new BigDecimal("340.00"), firstPayment.getAmount(), "Monthly payment should match");

        verify(kafkaProducer, times(3)).sendTo(any(), any());
    }
}
