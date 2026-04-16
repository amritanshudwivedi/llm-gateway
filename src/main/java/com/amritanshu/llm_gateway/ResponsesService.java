package com.amritanshu.llm_gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ResponsesService {
    private final OpenAiClient openAiClient;         // your M1 WebClient wrapper
    private final ResponsesCacheService cache;
    private final CacheKeyGenerator keyGenerator;
    private static final Logger log = LoggerFactory.getLogger(ResponsesService.class);

    public ResponsesService(OpenAiClient openAiClient, ResponsesCacheService cache, CacheKeyGenerator keyGenerator) {
        this.openAiClient = openAiClient;
        this.cache = cache;
        this.keyGenerator = keyGenerator;
    }

    public Mono<ResponsesResponse> complete(ResponsesRequest request) {
        String cacheKey = keyGenerator.generate(request);

        return cache.get(cacheKey)
                .doOnNext(r -> log.info("Cache HIT for key {}", cacheKey))
                .switchIfEmpty(
                        openAiClient.complete(request)
                                .flatMap(response ->
                                        cache.put(cacheKey, response)
                                                .thenReturn(response)
                                )
                                .doOnNext(r -> log.info("Cache MISS — fetched from OpenAI"))
                );
    }
}
