# Cohere4J Examples

These self-contained snippets demonstrate how to call the Cohere embeddings, rerank, and chat APIs using the generated Cohere4J client.

## Prerequisites
- Java 11+
- Cohere API key exported as `COHERE_API_KEY`
- (Optional) Override the default API base URL with `COHERE_BASE_URL`
- (Optional) Override models with `COHERE_EMBED_MODEL`, `COHERE_RERANK_MODEL`, or `COHERE_CHAT_MODEL`

Gradle tasks are provided so you can run each example without additional setup. The tasks automatically generate the client from the OpenAPI spec and compile the example sources.

## Run the examples

```bash
export COHERE_API_KEY=your_api_key_here

# Embeddings
./gradlew runEmbeddingExample

# Rerank
./gradlew runRerankExample

# Chat (non-streaming)
./gradlew runChatExample
```

Each task prints the result of the API call to standard output. See the corresponding `*.java` source files in this directory for the full examples.
