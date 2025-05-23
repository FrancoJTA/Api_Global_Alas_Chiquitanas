package com.example.global_api.Service;

import com.example.global_api.Class.User;
import com.example.global_api.Configuration.ExternalApiEndpoints;
import com.example.global_api.Configuration.ExternalApiProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class RegistroService {


    private static final Logger logger = LoggerFactory.getLogger(RegistroService.class);

    @Autowired
    private ExternalApiProperties apiProperties;

    @Autowired
    private ExternalApiEndpoints apiEndpoints;

    private final WebClient webClient;

    public RegistroService(WebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    public void printUrls() {
        logger.info("URLs completas (base URL + endpoint) cargadas desde properties:");
        List<String> baseUrls = apiProperties.getBaseUrls();
        List<String> endpoints = apiEndpoints.getEndpoints();

        for (int i = 0; i < baseUrls.size(); i++) {
            String fullUrl = baseUrls.get(i) + endpoints.get(i);
            logger.info(fullUrl);
        }
    }
    public boolean registroUsuario(User user, int origen) {
        AtomicBoolean allSuccess = new AtomicBoolean(true);

        for (int i = 0; i < apiProperties.getBaseUrls().size(); i++) {
            if (i == origen) {
                logger.info("Saltando API origen en índice {}.", i);
                continue; // saltar API origen
            }

            String baseUrl = apiProperties.getBaseUrls().get(i);
            String endpoint = apiEndpoints.getEndpoints().get(i);
            String url = baseUrl + endpoint;
            logger.info("Intentando registrar usuario en URL [{}] (índice {}).", url, i);
            try {
                if (endpoint.toLowerCase().contains("graphql")) {
                    // Construir cuerpo GraphQL
                    String mutation = "mutation Mutation($input: inputUsuarioGlobal) {" +
                            " nuevoUsuarioGlobal(input: $input) { id } }";

                    Map<String, Object> variables = new HashMap<>();
                    variables.put("input", userToMap(user));

                    Map<String, Object> body = new HashMap<>();
                    body.put("query", mutation);
                    body.put("variables", variables);

                    webClient.post()
                            .uri(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body)
                            .retrieve()
                            .onStatus(status -> status.isError(), response -> {
                                logger.error("Error en respuesta HTTP de GraphQL en URL [{}], status: {}", url, response.statusCode());
                                allSuccess.set(false);
                                return response.createException();
                            })
                            .bodyToMono(String.class)
                            .block();

                } else {
                    // REST POST con User directamente
                    webClient.post()
                            .uri(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(user)
                            .retrieve()
                            .onStatus(status -> status.isError(), response -> {
                                logger.error("Error en respuesta HTTP REST en URL [{}], status: {}", url, response.statusCode());
                                allSuccess.set(false);
                                return response.createException();
                            })
                            .bodyToMono(String.class)
                            .block();
                }
                logger.info("Registro exitoso en URL [{}]", url);
            } catch (WebClientResponseException ex) {
                allSuccess.set(false);
                logger.error("Fallo en la llamada WebClientResponseException para URL [{}]: Status {}, Body: {}", url, ex.getStatusCode(), ex.getResponseBodyAsString());
            } catch (Exception e) {
                allSuccess.set(false);
                logger.error("Error inesperado en la llamada a URL [{}]: {}", url, e.getMessage(), e);
            }
        }

        return allSuccess.get();
    }

    // Convierte User a Map<String,Object> para GraphQL variables
    private Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", user.nombre);
        map.put("apellido", user.apellido);
        map.put("email", user.email);
        map.put("telefono", user.telefono);
        map.put("ci", user.ci);
        map.put("password", user.password);
        return map;
    }
}
