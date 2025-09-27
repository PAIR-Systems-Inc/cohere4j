import ai.pairsys.cohere4j.client.ApiClient;
import ai.pairsys.cohere4j.client.ApiException;
import ai.pairsys.cohere4j.client.Configuration;
import ai.pairsys.cohere4j.client.api.DefaultApi;
import ai.pairsys.cohere4j.client.model.EmbedByTypeResponse;
import ai.pairsys.cohere4j.client.model.EmbedInputType;
import ai.pairsys.cohere4j.client.model.Embedv2Request;
import ai.pairsys.cohere4j.client.model.EmbeddingType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Example demonstrating all configurable parameters for the Cohere Embed API v2.
 * 
 * This example uses the v2 endpoint (/v2/embed) which provides:
 * - Enhanced model support with required input_type
 * - Configurable output dimensions
 * - Multiple embedding type outputs
 * - Token truncation controls
 * 
 * This example shows that enum parameters can be set in two ways:
 * 1. Using enum values directly for compile-time type safety
 * 2. Converting from strings using EnumType.fromValue() for dynamic configuration
 * 
 * Both approaches are valid and you can choose based on your needs.
 * 
 * Environment variables:
 * - COHERE_API_KEY: Your Cohere API key (required)
 */
public class EmbeddingExample {
    public static void main(String[] args) {
        String apiKey = System.getenv("COHERE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("Set the COHERE_API_KEY environment variable before running this example.");
            System.exit(1);
        }

        String model = "embed-english-light-v3.0";

        ApiClient client = Configuration.getDefaultApiClient();
        client.setBearerToken(apiKey);

        DefaultApi api = new DefaultApi(client);

        // Create and configure the embed v2 request
        Embedv2Request request = new Embedv2Request();
        
        // REQUIRED: Set the model
        request.setModel(model);
        
        // REQUIRED: Set the input type (required for v3 models)
        // Both enum and string values are supported:
        // Enum values: EmbedInputType.SEARCH_DOCUMENT, SEARCH_QUERY, CLASSIFICATION, CLUSTERING, IMAGE
        // String values: "search_document", "search_query", "classification", "clustering", "image"
        
        // Option 1: Using enum directly (compile-time type safety)
        request.setInputType(EmbedInputType.CLUSTERING);
        
        // Option 2: Converting from string (useful for dynamic configuration)
        // String inputTypeString = "search_document";
        // request.setInputType(EmbedInputType.fromValue(inputTypeString));
        
        // REQUIRED: Add text inputs to embed
        List<String> texts = Arrays.asList(
            "Hello, world!",
            "Goodmem is awesome."
        );
        request.setTexts(texts);
        
        // OPTIONAL: Set truncation strategy for long inputs
        // Both enum and string values are supported:
        // Enum values: Embedv2Request.TruncateEnum.NONE, START, END
        // String values: "NONE", "START", "END"
        // Default: END
        
        // Option 1: Using enum directly (compile-time type safety)
        request.setTruncate(Embedv2Request.TruncateEnum.END);
        
        // Option 2: Converting from string (useful for dynamic configuration)
        // String truncateString = "END";
        // request.setTruncate(Embedv2Request.TruncateEnum.fromValue(truncateString));
        
        // OPTIONAL: Specify embedding types to return
        // Both enum and string values are supported:
        // Enum values: EmbeddingType.FLOAT, INT8, UINT8, BINARY, UBINARY, BASE64
        // String values: "float", "int8", "uint8", "binary", "ubinary", "base64"
        // Default: returns only FLOAT embeddings
        
        // Option 1: Using enums directly (compile-time type safety)
        List<EmbeddingType> embeddingTypes = Arrays.asList(
            EmbeddingType.FLOAT
        );
        request.setEmbeddingTypes(embeddingTypes);
        
        // Option 2: Converting from strings (useful for dynamic configuration)
        // List<String> embeddingTypeStrings = Arrays.asList("float", "int8");
        // List<EmbeddingType> embeddingTypes = new ArrayList<>();
        // for (String typeStr : embeddingTypeStrings) {
        //     embeddingTypes.add(EmbeddingType.fromValue(typeStr));
        // }
        // request.setEmbeddingTypes(embeddingTypes);
        
        // OPTIONAL: Set maximum tokens for truncation
        // Only used when truncate is not NONE
        // request.setMaxTokens(1024);
        
        // OPTIONAL: Set output dimension for models that support it
        // This is only available for embed-v4 and newer models
        // Possible values are 256, 512, 1024, and 1536
        // request.setOutputDimension(1024);
        
        // OPTIONAL: Add image embeddings (base64 encoded)
        // List<String> images = Arrays.asList("base64_encoded_image_string");
        // request.setImages(images);

        try {
            EmbedByTypeResponse response = api.embedv2(null, request);
            // V2 response structure
            System.out.println("Embedding id: " + response.getId());
            
            List<String> availableTypes = new ArrayList<>();
            if (response.getEmbeddings().getFloat() != null) {
                availableTypes.add("float");
            }
            if (response.getEmbeddings().getInt8() != null) {
                availableTypes.add("int8");
            }
            if (response.getEmbeddings().getUint8() != null) {
                availableTypes.add("uint8");
            }
            if (response.getEmbeddings().getBinary() != null) {
                availableTypes.add("binary");
            }
            if (response.getEmbeddings().getUbinary() != null) {
                availableTypes.add("ubinary");
            }
            if (response.getEmbeddings().getBase64() != null) {
                availableTypes.add("base64");
            }
            System.out.println("Received typed embeddings for: " + String.join(", ", availableTypes));
            
            // Print float embeddings if available
            if (response.getEmbeddings().getFloat() != null) {
                List<List<BigDecimal>> floatEmbeddings = response.getEmbeddings().getFloat();
                System.out.println("Received " + floatEmbeddings.size() + " float embeddings");
                
                for (int i = 0; i < floatEmbeddings.size(); i++) {
                    List<BigDecimal> embedding = floatEmbeddings.get(i);
                    System.out.println("\n=== Embedding " + (i + 1) + " ===");
                    System.out.println("Text: \"" + request.getTexts().get(i) + "\"");
                    System.out.println("Vector dimensionality: " + embedding.size());
                    
                    // Print first 5 values
                    String preview = embedding.stream()
                            .limit(5)
                            .map(BigDecimal::toPlainString)
                            .collect(Collectors.joining(", "));
                    System.out.println("First 5 dimensions: [" + preview + ", ...]");
                }
            }
            
            // Show API metadata if available
            if (response.getMeta() != null && response.getMeta().getApiVersion() != null) {
                System.out.println("\nAPI Version: " + response.getMeta().getApiVersion().getVersion());
            }
            if (response.getMeta() != null && response.getMeta().getBilledUnits() != null) {
                System.out.println("Billed Units: " + response.getMeta().getBilledUnits().getInputTokens());
            }
        } catch (ApiException ex) {
            System.err.println("Embed request failed: " + ex.getCode() + " " + ex.getMessage());
            if (ex.getResponseBody() != null) {
                System.err.println(ex.getResponseBody());
            }
            System.exit(1);
        }
    }
}
