package com.example.global_api.Configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "external")
public class ExternalApiProperties {

    // Lista de URLs base (3 GraphQL, 2 REST)
    private List<String> baseUrls;

    // Getters y setters
    public List<String> getBaseUrls() {
        return baseUrls;
    }

    public void setBaseUrls(List<String> baseUrls) {
        this.baseUrls = baseUrls;
    }
}