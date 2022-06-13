package de.earlgreyt.date4u;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Date4uApplication {

    public static void main(String[] args) {
        SpringApplication.run(Date4uApplication.class, args);
    }

}
