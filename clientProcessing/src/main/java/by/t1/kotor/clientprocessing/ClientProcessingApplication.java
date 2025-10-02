package by.t1.kotor.clientprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"by.t1.kotor.clientprocessing", "by.t1.kotor.common"})
public class ClientProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientProcessingApplication.class, args);
    }

}
