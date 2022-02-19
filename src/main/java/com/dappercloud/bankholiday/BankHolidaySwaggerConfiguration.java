package com.dappercloud.bankholiday;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankHolidaySwaggerConfiguration {
  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("united-states")
        .pathsToMatch("/united-states/**")
        .build();
  }
}
