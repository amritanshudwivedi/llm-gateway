package com.amritanshu.llm_gateway;

import java.util.List;

public record ResponsesResponse(
        String id,
        String object,
        List<OutputItem> output
) {
    public record OutputItem(
            String type,
            List<ContentItem> content
    ) {}

    public record ContentItem(
            String type,
            String text
    ) {}
}
