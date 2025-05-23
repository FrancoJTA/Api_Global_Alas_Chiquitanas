package com.example.global_api;

import com.example.global_api.Configuration.ExternalApiEndpoints;
import com.example.global_api.Configuration.ExternalApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ExternalApiProperties.class, ExternalApiEndpoints.class})
public class GlobalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobalApiApplication.class, args);
    }

}
