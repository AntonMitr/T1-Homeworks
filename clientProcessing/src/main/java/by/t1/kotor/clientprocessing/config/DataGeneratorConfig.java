package by.t1.kotor.clientprocessing.config;

import by.t1.kotor.clientprocessing.model.Client;
import by.t1.kotor.clientprocessing.model.ClientProduct;
import by.t1.kotor.clientprocessing.model.Product;
import by.t1.kotor.clientprocessing.model.User;
import by.t1.kotor.clientprocessing.model.enums.DocumentTypeEnum;
import by.t1.kotor.clientprocessing.model.enums.KeyEnum;
import by.t1.kotor.clientprocessing.model.enums.StatusEnum;
import by.t1.kotor.clientprocessing.repository.ClientProductRepository;
import by.t1.kotor.clientprocessing.repository.ClientRepository;
import by.t1.kotor.clientprocessing.repository.ProductRepository;
import by.t1.kotor.clientprocessing.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Configuration
public class DataGeneratorConfig {

    @Bean
    public CommandLineRunner generateClientData(
            UserRepository userRepository,
            ClientRepository clientRepository,
            ProductRepository productRepository,
            ClientProductRepository clientProductRepository
    ) {
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();
            Set<String> usedLogins = new HashSet<>();
            Set<String> usedEmails = new HashSet<>();
            Set<String> usedDocumentIds = new HashSet<>();

            Product[] products = new Product[5];
            for (int i = 0; i < products.length; i++) {
                Product product = new Product();
                product.setName(faker.commerce().productName());
                product.setKey(faker.options().option(KeyEnum.class));
                product.setCreateDate(LocalDateTime.now().minusDays(random.nextInt(365)));
                productRepository.save(product);
                products[i] = product;
            }

            for (int i = 0; i < 10; i++) {
                User user = new User();
                String login;
                do { login = faker.name().username(); } while (usedLogins.contains(login));
                usedLogins.add(login);
                user.setLogin(login);

                String email;
                do { email = faker.internet().emailAddress(); } while (usedEmails.contains(email));
                usedEmails.add(email);
                user.setEmail(email);
                user.setPassword(faker.internet().password(8, 16));
                userRepository.save(user);

                Client client = new Client();
                client.setUser(user);
                client.setClientId(faker.idNumber().valid());
                client.setFirstName(faker.name().firstName());
                client.setMiddleName(faker.name().firstName());
                client.setLastName(faker.name().lastName());
                client.setDateOfBirth(LocalDate.now().minusYears(random.nextInt(50) + 18));
                client.setDocumentType(faker.options().option(DocumentTypeEnum.class));

                String documentId;
                do { documentId = faker.idNumber().valid(); } while (usedDocumentIds.contains(documentId));
                usedDocumentIds.add(documentId);

                client.setDocumentId(documentId);
                client.setDocumentPrefix(faker.letterify("??"));
                client.setDocumentSuffix(faker.letterify("??"));
                clientRepository.save(client);

                int productCount = random.nextInt(3) + 1;
                for (int p = 0; p < productCount; p++) {
                    Product product = products[random.nextInt(products.length)];

                    ClientProduct clientProduct = new ClientProduct();
                    clientProduct.setClient(client);
                    clientProduct.setProduct(product);
                    clientProduct.setOpenDate(LocalDate.now().minusDays(random.nextInt(365)));
                    clientProduct.setCloseDate(random.nextBoolean() ? LocalDate.now().minusDays(random.nextInt(100)) : null);
                    clientProduct.setStatus(faker.options().option(StatusEnum.class));
                    clientProductRepository.save(clientProduct);
                }
            }
        };
    }
}
