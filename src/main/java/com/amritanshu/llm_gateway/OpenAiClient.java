package com.amritanshu.llm_gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenAiClient {

    private final WebClient webClient;

    public OpenAiClient(@Value("${openai.apikey}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    public Mono<ResponsesResponse> complete(ResponsesRequest request) {
        return webClient.post()
                .uri("/v1/responses")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ResponsesResponse.class); // no ResponseEntity wrapper
    }
}
