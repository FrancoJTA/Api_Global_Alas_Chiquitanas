package com.example.global_api.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "external")
public class ExternalApiEndpoints {

    // Endpoints para cada API
    private List<String> endpoints;

    // Getters y setters
    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }
}