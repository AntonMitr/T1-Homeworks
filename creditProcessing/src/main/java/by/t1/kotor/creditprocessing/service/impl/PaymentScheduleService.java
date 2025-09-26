package by.t1.kotor.creditprocessing.service.impl;

import by.t1.kotor.creditprocessing.model.PaymentRegistry;
import by.t1.kotor.creditprocessing.model.ProductRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PaymentScheduleService {

    public List<PaymentRegistry> generateSchedule(ProductRegistry productRegistry,
                                                  BigDecimal creditAmount,
                                                  BigDecimal interestRate,
                                                  int monthCount) {
        log.debug("Start generateSchedule for productRegistryId={}, creditAmount={}, interestRate={}, monthCount={}",
                productRegistry.getId(), creditAmount, interestRate, monthCount);

        List<PaymentRegistry> schedule = new ArrayList<>();
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(12 * 100L), 3, RoundingMode.HALF_UP);
        BigDecimal factor = (BigDecimal.ONE.add(monthlyRate)).pow(monthCount);
        BigDecimal monthlyPayment = creditAmount.multiply(monthlyRate.multiply(factor)
                .divide(factor.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP));

        BigDecimal balance = creditAmount;
        for (int month = 1; month <= monthCount; month++) {
            BigDecimal interest = balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principal = monthlyPayment.subtract(interest).setScale(2, RoundingMode.HALF_UP);
            balance = balance.subtract(principal).setScale(2, RoundingMode.HALF_UP);

            PaymentRegistry payment = PaymentRegistry.builder()
                    .productRegistry(productRegistry)
                    .paymentDate(productRegistry.getOpenDate().plusMonths(month - 1))
                    .amount(monthlyPayment)
                    .interestRateAmount(interest)
                    .debtAmount(principal)
                    .expired(false)
                    .paymentExpirationDate(productRegistry.getOpenDate().plusMonths(month))
                    .build();

            schedule.add(payment);
        }
        log.debug("Generated {} payment schedules for productRegistryId={}", schedule.size(), productRegistry.getId());
        return schedule;
    }
}
