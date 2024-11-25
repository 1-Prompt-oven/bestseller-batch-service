package com.promptoven.sellerbatchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SellerbatchserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SellerbatchserviceApplication.class, args);
    }

}
