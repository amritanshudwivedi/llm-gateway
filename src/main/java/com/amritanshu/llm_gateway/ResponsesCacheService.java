package com.amritanshu.llm_gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class ResponsesCacheService {

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Duration TTL = Duration.ofHours(24);

    public ResponsesCacheService(@Qualifier("reactiveStringRedisTemplate")  ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<ResponsesResponse> get(String key) {
        return redisTemplate.opsForValue().get(key)
                .flatMap(json -> {
                    try {
                        return Mono.just(objectMapper.readValue(json, ResponsesResponse.class));
                    } catch (Exception e) {
                        log.warn("Error reading from cache", e);
                        return Mono.empty(); // corrupt cache entry → treat as miss
                    }
                });
    }

    public Mono<Void> put(String key, ResponsesResponse response) {
        try {
            String json = objectMapper.writeValueAsString(response);
            return redisTemplate.opsForValue().set(key, json, TTL).then();
        } catch (Exception e) {
            log.warn("Error writing to cache", e);
            return Mono.empty(); // cache write failure should never break the request
        }
    }
}
