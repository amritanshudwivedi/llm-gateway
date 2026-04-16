package com.amritanshu.llm_gateway;

public record ResponsesRequest(
        String model,
        String input,
        Double temperature,
        String instructions
) {}
