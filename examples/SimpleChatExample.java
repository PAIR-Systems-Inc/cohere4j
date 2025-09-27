import ai.pairsys.cohere4j.client.ApiClient;
import ai.pairsys.cohere4j.client.ApiException;
import ai.pairsys.cohere4j.client.Configuration;
import ai.pairsys.cohere4j.client.api.DefaultApi;
import ai.pairsys.cohere4j.client.model.Chat200Response;
import ai.pairsys.cohere4j.client.model.ChatRequest;
import ai.pairsys.cohere4j.client.model.NonStreamedChatResponse;

/**
 * Simple Chat Example demonstrating basic chat functionality with Cohere API.
 * 
 * Environment variables:
 * - COHERE_API_KEY: Your Cohere API key (required)
 */
public class SimpleChatExample {
    public static void main(String[] args) {
        String apiKey = System.getenv("COHERE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("Set the COHERE_API_KEY environment variable before running this example.");
            System.exit(1);
        }

        String model = "command-r-08-2024";

        ApiClient client = Configuration.getDefaultApiClient();
        // Base URL is already set to https://api.cohere.com in the generated ApiClient
        client.setBearerToken(apiKey);

        DefaultApi api = new DefaultApi(client);

        // Create a simple chat request with minimal parameters
        ChatRequest request = new ChatRequest()
                .message("What are the main features of the Cohere API?")
                .model(model)
                .stream(false);

        try {
            Chat200Response response = api.chat(null, null, request);
            Object actual = response.getActualInstance();
            if (actual instanceof NonStreamedChatResponse) {
                NonStreamedChatResponse nonStreamed = (NonStreamedChatResponse) actual;
                
                System.out.println("=== Chat Response ===");
                System.out.println("Assistant: " + nonStreamed.getText());
                
                if (nonStreamed.getGenerationId() != null) {
                    System.out.println("\nGeneration ID: " + nonStreamed.getGenerationId());
                }
                
                // Token usage information
                if (nonStreamed.getMeta() != null && nonStreamed.getMeta().getTokens() != null) {
                    System.out.println("\nToken Usage:");
                    System.out.println("  Input tokens: " + nonStreamed.getMeta().getTokens().getInputTokens());
                    System.out.println("  Output tokens: " + nonStreamed.getMeta().getTokens().getOutputTokens());
                }
            } else {
                System.out.println("Unexpected response type: " + 
                    (actual != null ? actual.getClass().getSimpleName() : "null"));
            }
        } catch (ApiException ex) {
            System.err.println("Chat request failed: " + ex.getCode() + " " + ex.getMessage());
            if (ex.getResponseBody() != null) {
                System.err.println(ex.getResponseBody());
            }
            System.exit(1);
        }
    }
}