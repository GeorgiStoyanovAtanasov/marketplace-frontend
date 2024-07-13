package com.example.demo;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdHJpbmdqZGJnQGdtYWlsLmNvbSIsImlhdCI6MTcyMDg1OTQ3MiwiZXhwIjoxNzIwODYzMDcyfQ.XQ91T2gjwVa4vFVSR8IAzh5XuIEySMEhRhUjv7-hm8A");
        };
    }
}

