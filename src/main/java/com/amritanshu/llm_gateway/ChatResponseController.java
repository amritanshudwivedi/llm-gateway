package com.amritanshu.llm_gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class ChatResponseController {

    private final WebClient webClient;

    public ChatResponseController(@Value("${openai.apikey}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @PostMapping("/responses")
    public Mono<ResponseEntity<String>> forward(@RequestBody String body) {
        return webClient.post()
                .uri("/v1/responses")
                .bodyValue(body)
                .retrieve()
                .toEntity(String.class);
    }
}
