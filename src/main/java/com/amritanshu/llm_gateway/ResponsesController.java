package com.amritanshu.llm_gateway;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class ResponsesController {
    
    ResponsesService responsesService;

    public ResponsesController(ResponsesService responsesService) {
        this.responsesService = responsesService;
    }

    @PostMapping("/responses")
    public Mono<ResponsesResponse> forward(@RequestBody ResponsesRequest responsesRequest) {
        return responsesService.complete(responsesRequest);
    }
}
