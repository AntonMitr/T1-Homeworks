package by.t1.kotor.creditprocessing.config;

import by.t1.kotor.creditprocessing.model.PaymentRegistry;
import by.t1.kotor.creditprocessing.model.ProductRegistry;
import by.t1.kotor.creditprocessing.repository.PaymentRegistryRepository;
import by.t1.kotor.creditprocessing.repository.ProductRegistryRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Configuration
public class DataGeneratorConfig {
    @Bean
    public CommandLineRunner generateCreditData(
            ProductRegistryRepository productRegistryRepository,
            PaymentRegistryRepository paymentRegistryRepository
    ) {
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();

            for (int i = 0; i < 10; i++) {
                ProductRegistry productRegistry = new ProductRegistry();
                productRegistry.setClientId((long) faker.number().numberBetween(1000, 2000));
                productRegistry.setAccountId((long) faker.number().numberBetween(1, 20));
                productRegistry.setProductId((long) faker.number().numberBetween(1, 10));
                productRegistry.setInterestRate(BigDecimal.valueOf(faker.number().randomDouble(4, 1, 10) / 100));
                productRegistry.setOpenDate(LocalDate.now().minusDays(random.nextInt(365)));
                productRegistryRepository.save(productRegistry);

                int paymentCount = random.nextInt(5) + 3;
                for (int j = 0; j < paymentCount; j++) {
                    PaymentRegistry payment = new PaymentRegistry();
                    payment.setProductRegistry(productRegistry);
                    payment.setPaymentDate(LocalDate.now().minusDays(random.nextInt(180)));
                    payment.setAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 100, 5000)));
                    payment.setInterestRateAmount(BigDecimal.valueOf(faker.number().randomDouble(4, 1, 10) / 100));
                    payment.setDebtAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 0, 1000)));
                    payment.setExpired(random.nextBoolean());
                    payment.setPaymentExpirationDate(LocalDate.now().plusDays(random.nextInt(60)));
                    paymentRegistryRepository.save(payment);
                }
            }
        };
    }
}
