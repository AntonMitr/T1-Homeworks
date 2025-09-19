package by.t1.kotor.accountprocessing.config;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.model.Card;
import by.t1.kotor.accountprocessing.model.Payment;
import by.t1.kotor.accountprocessing.model.Transaction;
import by.t1.kotor.accountprocessing.model.enums.*;
import by.t1.kotor.accountprocessing.repository.AccountRepository;
import by.t1.kotor.accountprocessing.repository.CardRepository;
import by.t1.kotor.accountprocessing.repository.PaymentRepository;
import by.t1.kotor.accountprocessing.repository.TransactionRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Configuration
public class DataGeneratorConfig {

    @Bean
    public CommandLineRunner loadData(
            AccountRepository accountRepository,
            CardRepository cardRepository,
            PaymentRepository paymentRepository,
            TransactionRepository transactionRepository) {

        return args -> {
            Faker faker = new Faker();
            Random random = new Random();

            for (int i = 0; i < 10; i++) {
                Account account = new Account();
                account.setClientId((long) faker.number().numberBetween(1000, 2000));
                account.setProductId((long) faker.number().numberBetween(2000, 3000));
                account.setBalance(BigDecimal.valueOf(faker.number().randomDouble(2, 100, 5000)));
                account.setInterestRate(BigDecimal.valueOf(faker.number().randomDouble(4, 1, 5) / 100));
                account.setIsRecalc(random.nextBoolean());
                account.setCardExist(true);
                account.setStatus(faker.options().option(AccountStatusEnum.class));
                accountRepository.save(account);

                int cardCount = random.nextInt(3) + 1;
                for (int c = 0; c < cardCount; c++) {
                    Card card = new Card();
                    card.setAccount(account);
                    card.setCardId(faker.finance().creditCard().replaceAll("-", ""));
                    card.setPaymentSystem(faker.options().option(PaymentSystemEnum.class));
                    card.setStatus(faker.options().option(CardStatusEnum.class));
                    cardRepository.save(card);

                    int transactionCount = random.nextInt(6) + 5;
                    for (int t = 0; t < transactionCount; t++) {
                        Transaction transaction = new Transaction();
                        transaction.setAccount(account);
                        transaction.setCard(card);
                        transaction.setType(faker.options().option(TransactionTypeEnum.class));
                        transaction.setAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 5, 1000)));
                        transaction.setStatus(faker.options().option(TransactionStatusEnum.class));
                        transaction.setTimestamp(LocalDateTime.now().minusHours(random.nextInt(720)));
                        transactionRepository.save(transaction);
                    }
                }

                int paymentCount = random.nextInt(4) + 2;
                for (int p = 0; p < paymentCount; p++) {
                    Payment payment = new Payment();
                    payment.setAccount(account);
                    payment.setPaymentDate(LocalDate.now().minusDays(random.nextInt(60)));
                    payment.setAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 2000)));
                    payment.setIsCredit(random.nextBoolean());
                    payment.setPayedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
                    payment.setType(faker.options().option(PaymentTypeEnum.class));
                    paymentRepository.save(payment);
                }
            }
        };
    }
}
