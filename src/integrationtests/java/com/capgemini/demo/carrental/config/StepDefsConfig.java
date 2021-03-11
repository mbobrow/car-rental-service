package com.capgemini.demo.carrental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"com.capgemini.demo.carrental"})
public class StepDefsConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
