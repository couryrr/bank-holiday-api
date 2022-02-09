package com.dappercloud.bankholiday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableScheduling
@EnableCaching
public class BankHolidayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankHolidayApplication.class, args);
    }


}
