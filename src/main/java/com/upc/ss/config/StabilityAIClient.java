package com.upc.ss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class StabilityAIClient {
    @Value("${stability.api.key}")
    private String apiKey;

    @Value("${stability.api.url}")
    private String apiUrl;

    private final WebClient webClient;

    public StabilityAIClient() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(config -> config
                        .defaultCodecs()
                        .maxInMemorySize(20 * 1024 * 1024))
                .build();

        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }

    public String generarImagen(String prompt) {

        Map<String, Object> body = Map.of(
                "text_prompts", List.of(Map.of("text", prompt, "weight", 1)),
                "cfg_scale", 7,
                "height", 1024,
                "width", 1024,
                "samples", 1,
                "steps", 30
        );

        Map response = webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.ACCEPT, "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, Object>> artifacts =
                (List<Map<String, Object>>) response.get("artifacts");

        String base64Image = (String) artifacts.get(0).get("base64");

        // Devuelve el base64 con prefijo
        return "data:image/png;base64," + base64Image;
    }
}
