# LLM Gateway

A Spring Boot service that proxies chat completion requests to the OpenAI API.

## What it does
Accepts a POST request to `/v1/responses` and forwards it to OpenAI,
returning the response as-is. No caching or rate limiting yet (coming in M2/M3).

## How to run

1. Clone the repo
2. Create a `.env` file in the project root:
   OPENAI_API_KEY=sk-your-key-here
3. Run the app:
```bash
   ./gradlew bootRun
```

## Example request

```bash
curl -X POST http://localhost:8080/v1/responses \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gpt-5-nano",
    "input": "write a haiku about ai",
    "store": true
  }'
```

## Tech stack
- Java 17
- Spring Boot (WebFlux)
- OpenAI Chat Completions API