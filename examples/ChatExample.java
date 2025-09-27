import ai.pairsys.cohere4j.client.ApiClient;
import ai.pairsys.cohere4j.client.ApiException;
import ai.pairsys.cohere4j.client.Configuration;
import ai.pairsys.cohere4j.client.api.DefaultApi;
import ai.pairsys.cohere4j.client.model.Chatv2200Response;
import ai.pairsys.cohere4j.client.model.Chatv2Request;
import ai.pairsys.cohere4j.client.model.ChatMessageV2;
import ai.pairsys.cohere4j.client.model.UserMessageV2;
import ai.pairsys.cohere4j.client.model.AssistantMessageV2;
import ai.pairsys.cohere4j.client.model.AssistantMessageV2Content;
import ai.pairsys.cohere4j.client.model.SystemMessageV2;
import ai.pairsys.cohere4j.client.model.SystemMessageV2Content;
import ai.pairsys.cohere4j.client.model.UserMessageV2Content;
import ai.pairsys.cohere4j.client.model.ToolV2;
// import ai.pairsys.cohere4j.client.model.ToolV2ParameterDefinitions; // Not needed for this example
import ai.pairsys.cohere4j.client.model.ChatResponseV2;
import ai.pairsys.cohere4j.client.model.StreamedChatResponseV2;
import ai.pairsys.cohere4j.client.model.CitationOptions;
import ai.pairsys.cohere4j.client.model.Chatv2RequestDocumentsInner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example demonstrating all configurable parameters for the Cohere Chat API v2.
 * 
 * This example uses the v2 endpoint (/v2/chat) which provides:
 * - Enhanced message structure with role-based messages
 * - Improved tool calling capabilities
 * - Better streaming support
 * - Citation control options
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
public class ChatExample {
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

        // Create and configure the chat v2 request
        Chatv2Request request = new Chatv2Request();
        
        // REQUIRED: Set the model
        request.setModel(model);
        
        // REQUIRED: Set the messages (v2 uses a message list instead of a single message)
        List<ChatMessageV2> messages = new ArrayList<>();
        
        // Add a system message (optional)
        SystemMessageV2 systemMessage = new SystemMessageV2();
        systemMessage.setRole(SystemMessageV2.RoleEnum.SYSTEM);
        systemMessage.setContent(new SystemMessageV2Content("You are a helpful AI assistant with expertise in Cohere's APIs."));
        messages.add(new ChatMessageV2(systemMessage));
        
        // Add a user message
        UserMessageV2 userMessage = new UserMessageV2();
        userMessage.setRole(UserMessageV2.RoleEnum.USER);
        userMessage.setContent(new UserMessageV2Content("Tell me about Cohere's AI capabilities and give me a code example."));
        messages.add(new ChatMessageV2(userMessage));
        
        request.setMessages(messages);
        
        // OPTIONAL: Enable/disable streaming
        // Default: false
        request.setStream(false);
        
        // OPTIONAL: Enable thinking mode (for models that support it)
        // request.setThinking(new Thinking().thinking(true));
        
        // OPTIONAL: Add conversation history by adding more messages to the list
        // UserMessageV2 previousUser = new UserMessageV2();
        // previousUser.setRole(UserMessageV2.RoleEnum.USER);
        // previousUser.setContent(new UserMessageV2Content("What can Cohere do?"));
        // messages.add(0, new ChatMessageV2(previousUser));
        // 
        // AssistantMessageV2 previousAssistant = new AssistantMessageV2();
        // previousAssistant.setRole(AssistantMessageV2.RoleEnum.ASSISTANT);
        // previousAssistant.setContent(new AssistantMessageV2Content("Cohere provides powerful AI APIs."));
        // messages.add(1, new ChatMessageV2(previousAssistant));
        
        // OPTIONAL: Documents for RAG (v2 uses a different document structure)
        // List<Chatv2RequestDocumentsInner> documents = Arrays.asList(
        //     new Chatv2RequestDocumentsInner()
        //         .id("doc1")
        //         .putAdditionalProperty("title", "Cohere Documentation")
        //         .putAdditionalProperty("text", "Cohere offers state-of-the-art language models."),
        //     new Chatv2RequestDocumentsInner()
        //         .id("doc2")
        //         .putAdditionalProperty("title", "API Guide")
        //         .putAdditionalProperty("text", "The Chat API supports streaming and tool calling.")
        // );
        // request.setDocuments(documents);
        
        // OPTIONAL: Citation options (v2 uses a structured object)
        CitationOptions citationOptions = new CitationOptions();
        citationOptions.setMode(CitationOptions.ModeEnum.ACCURATE);
        request.setCitationOptions(citationOptions);
        
        // OPTIONAL: Maximum tokens to generate
        request.setMaxTokens(500);
        
        // NOTE: maxInputTokens is not available in v2 API
        // request.setMaxInputTokens(4096);
        
        // OPTIONAL: Temperature for randomness
        // Range: 0-1, Default: 0.3
        request.setTemperature(0.7f);
        
        // OPTIONAL: Maximum tokens to generate
        request.setMaxTokens(500);
        
        // NOTE: maxInputTokens is not available in v2 API
        // request.setMaxInputTokens(4096);
        
        // OPTIONAL: Top-k sampling
        // Range: 0-500, Default: 0
        request.setK(0);
        
        // OPTIONAL: Nucleus sampling
        // Range: 0.01-0.99, Default: 0.75
        request.setP(0.75f);
        
        // OPTIONAL: Random seed for deterministic output
        // Range: 0-384
        request.setSeed(42);
        
        // OPTIONAL: Stop sequences
        List<String> stopSequences = Arrays.asList("\n\n");
        request.setStopSequences(stopSequences);
        
        // OPTIONAL: Frequency penalty
        // Range: 0-1, Default: 0
        request.setFrequencyPenalty(0.1f);
        
        // OPTIONAL: Presence penalty  
        // Range: 0-1, Default: 0
        request.setPresencePenalty(0.1f);
        
        // OPTIONAL: Tools/Functions (v2 uses ToolV2)
        // List<ToolV2> tools = Arrays.asList(
        //     new ToolV2()
        //         .name("get_weather")
        //         .description("Get weather information for a city")
        //         .parameterSchema(createWeatherToolSchema())
        // );
        // request.setTools(tools);
        // 
        // // OPTIONAL: Strict tools mode
        // // request.setStrictTools(true);
        
        
        // OPTIONAL: Safety mode
        // Both enum and string values are supported:
        // Enum values: Chatv2Request.SafetyModeEnum.CONTEXTUAL, STRICT, FALSE
        // String values: "CONTEXTUAL", "STRICT", "false"
        // Default: CONTEXTUAL
        
        // Option 1: Using enum directly (compile-time type safety)
        request.setSafetyMode(Chatv2Request.SafetyModeEnum.CONTEXTUAL);
        
        // Option 2: Converting from string (useful for dynamic configuration)
        // String safetyModeString = "CONTEXTUAL";
        // request.setSafetyMode(Chatv2Request.SafetyModeEnum.fromValue(safetyModeString));
        
        // OPTIONAL: Response format for structured outputs (v2 uses ResponseFormatV2)
        // ResponseFormatV2 responseFormat = new ResponseFormatV2();
        // responseFormat.setType(ResponseFormatV2.TypeEnum.TEXT);
        // request.setResponseFormat(responseFormat);

        try {
            Chatv2200Response response = api.chatv2(null, request);
            Object actual = response.getActualInstance();
            if (actual instanceof ChatResponseV2) {
                ChatResponseV2 nonStreamed = (ChatResponseV2) actual;
                System.out.println("\n=== Chat Response ===");
                if (nonStreamed.getMessage() != null && nonStreamed.getMessage().getContent() != null) {
                    System.out.println("Assistant: " + nonStreamed.getMessage().getContent());
                }
                
                // Display generation metadata
                if (nonStreamed.getId() != null) {
                    System.out.println("\nResponse ID: " + nonStreamed.getId());
                }
                
                // Token usage information
                if (nonStreamed.getUsage() != null && nonStreamed.getUsage().getTokens() != null) {
                    System.out.println("\nToken Usage:");
                    System.out.println("  Input tokens: " + nonStreamed.getUsage().getTokens().getInputTokens());
                    System.out.println("  Output tokens: " + nonStreamed.getUsage().getTokens().getOutputTokens());
                    System.out.println("  Total tokens: " + 
                        (nonStreamed.getUsage().getTokens().getInputTokens().add(
                         nonStreamed.getUsage().getTokens().getOutputTokens())));
                }
                // Display tool calls if any
                if (nonStreamed.getMessage() != null && 
                    nonStreamed.getMessage().getToolCalls() != null && 
                    !nonStreamed.getMessage().getToolCalls().isEmpty()) {
                    System.out.println("\nTool calls:");
                    nonStreamed.getMessage().getToolCalls().forEach(call -> {
                        System.out.println("  - Function: " + call.getFunction().getName());
                        System.out.println("    Parameters: " + call.getFunction().getArguments());
                    });
                }
                
                // NOTE: Meta information is not available on the Chatv2200Response wrapper
                // Metadata like API version and billed units are typically in response headers
            } else if (actual instanceof StreamedChatResponseV2) {
                StreamedChatResponseV2 streamed = (StreamedChatResponseV2) actual;
                System.out.println("Received streamed event (streaming not implemented in this example)");
            } else if (actual != null) {
                System.out.println("Unexpected chat response type: " + actual.getClass().getSimpleName());
            }
        } catch (ApiException ex) {
            System.err.println("Chat request failed: " + ex.getCode() + " " + ex.getMessage());
            if (ex.getResponseBody() != null) {
                System.err.println(ex.getResponseBody());
            }
            System.exit(1);
        }
    }
    
    private static Map<String, Object> createWeatherToolSchema() {
        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");
        
        Map<String, Object> properties = new HashMap<>();
        
        // Define 'city' parameter
        Map<String, Object> cityParam = new HashMap<>();
        cityParam.put("type", "string");
        cityParam.put("description", "The city to get weather for");
        properties.put("city", cityParam);
        
        // Define 'unit' parameter
        Map<String, Object> unitParam = new HashMap<>();
        unitParam.put("type", "string");
        unitParam.put("description", "Temperature unit (celsius or fahrenheit)");
        unitParam.put("enum", Arrays.asList("celsius", "fahrenheit"));
        properties.put("unit", unitParam);
        
        schema.put("properties", properties);
        schema.put("required", Arrays.asList("city"));
        
        return schema;
    }
}
