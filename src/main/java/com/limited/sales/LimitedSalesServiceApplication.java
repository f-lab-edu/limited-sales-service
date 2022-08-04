package com.limited.sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true)
public class LimitedSalesServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(LimitedSalesServiceApplication.class, args);
  }
}
