package com.amritanshu.llm_gateway;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class CacheKeyGenerator {

    public String generate(ResponsesRequest request) {
        String raw = request.model()
                + "|" + (request.temperature() != null ? request.temperature() : "default")
                + "|" + (request.instructions() != null ? request.instructions() : "")
                + "|" + request.input();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return "llm:cache:" + HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
