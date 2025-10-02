package by.t1.kotor.accountprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"by.t1.kotor.accountprocessing", "by.t1.kotor.common"})
public class AccountProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountProcessingApplication.class, args);
    }

}
