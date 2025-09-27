# Cohere4J

[![JitPack](https://jitpack.io/v/PAIR-Systems-Inc/cohere4j.svg)](https://jitpack.io/#PAIR-Systems-Inc/cohere4j)

Cohere4J is a Java client for Cohere's embeddings, rerank, and chat APIs. The client is generated directly from Cohere's OpenAPI specification so you always get full, type-safe coverage of the platform.

## Features
- End-to-end coverage for Cohere embeddings, rerank, generative chat, finetuning, and model management endpoints
- Type-safe models generated from the official OpenAPI specification
- Java 11+ compatible, with OkHttp and Gson under the hood
- Ready-to-run examples for embeddings, rerank, and chat in the [`examples/`](examples) directory

## Installation

### Gradle
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.PAIR-Systems-Inc:cohere4j:main-SNAPSHOT' // Latest development build
    // or pin to a release tag, e.g. 'com.github.PAIR-Systems-Inc:cohere4j:0.1.0'
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.PAIR-Systems-Inc</groupId>
        <artifactId>cohere4j</artifactId>
        <version>main-SNAPSHOT</version>
        <!-- or use a tagged release: <version>0.1.0</version> -->
    </dependency>
</dependencies>
```

## API version

Cohere has two API versions, v1 and v2. v1 is being phased out. So this library supports only v2. 

## Authentication

Set up authentication using your Cohere API key:

```java
import ai.pairsys.cohere4j.client.ApiClient;
import ai.pairsys.cohere4j.client.Configuration;

// Get your API key from environment variable
String apiKey = System.getenv("COHERE_API_KEY");

// Create and configure the API client
ApiClient client = Configuration.getDefaultApiClient();
client.setBearerToken(apiKey);

```

## Examples
Sample applications live in [`examples/`](examples). Each example demonstrates all available parameters and includes both enum and string-based configuration options. Run them after setting your `COHERE_API_KEY` environment variable.

### Available Examples
- **EmbeddingExample**: Demonstrates text embeddings with all configuration options
- **RerankExample**: Shows document reranking with relevance scoring
- **ChatExample**: Illustrates conversational AI with all available parameters

## Building From Source
```bash
git clone https://github.com/PAIR-Systems-Inc/cohere4j.git
cd cohere4j

# Generate the client and run the build
./gradlew build
```

## Requirements
- Java 11+
- Cohere API key

## Contributing
1. Fork the repository
2. Create a feature branch
3. Run `./gradlew build`
4. Add/adjust tests or examples if applicable
5. Open a pull request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support
- [GitHub Issues](https://github.com/PAIR-Systems-Inc/cohere4j/issues)
- Cohere Docs
  * [Reranking v2 API](https://docs.cohere.com/reference/rerank)
  * [Embedding v2 API](https://docs.cohere.com/reference/embed)
