import ai.pairsys.cohere4j.client.ApiClient;
import ai.pairsys.cohere4j.client.ApiException;
import ai.pairsys.cohere4j.client.Configuration;
import ai.pairsys.cohere4j.client.api.DefaultApi;
import ai.pairsys.cohere4j.client.model.Rerankv2200Response;
import ai.pairsys.cohere4j.client.model.Rerankv2200ResponseResultsInner;
import ai.pairsys.cohere4j.client.model.Rerankv2Request;

import java.util.Arrays;
import java.util.List;

/**
 * Example demonstrating all configurable parameters for the Cohere Rerank API v2.
 * 
 * This example uses the v2 endpoint (/v2/rerank) which provides:
 * - Simplified document format (strings only)
 * - max_tokens_per_doc parameter for document truncation
 * - Streamlined response format
 * 
 * Environment variables:
 * - COHERE_API_KEY: Your Cohere API key (required)
 */
public class RerankExample {
    public static void main(String[] args) {
        String apiKey = System.getenv("COHERE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("Set the COHERE_API_KEY environment variable before running this example.");
            System.exit(1);
        }

        String model = "rerank-english-v3.0";

        ApiClient client = Configuration.getDefaultApiClient();
        // Base URL is already set to https://api.cohere.com in the generated ApiClient
        client.setBearerToken(apiKey);

        DefaultApi api = new DefaultApi(client);

        // Create and configure the rerank v2 request
        Rerankv2Request request = new Rerankv2Request();
        
        // REQUIRED: Set the model
        request.setModel(model);
        
        // REQUIRED: Set the query to rerank documents against
        request.setQuery("What is the capital of France?");
        
        // REQUIRED: Add documents to rerank (v2 accepts strings only)
        List<String> documents = Arrays.asList(
            "Madrid is the capital of Spain",
            "France is a country in Europe",
            "Paris to France is like Rome to Italy",
            "Berlin is the capital of Germany",
            "Paris is the capital and largest city of France"
        );
        request.setDocuments(documents);
        
        // OPTIONAL: Limit number of results
        // Default: returns all documents
        request.setTopN(3);
        
        // OPTIONAL: Maximum tokens per document
        // Long documents will be automatically truncated to this number of tokens
        // Default: 4096
        request.setMaxTokensPerDoc(4096);

        try {
            Rerankv2200Response response = api.rerankv2(null, request);
            List<Rerankv2200ResponseResultsInner> results = response.getResults();
            System.out.println("\nRerank Results:");
            System.out.println("Query: \"" + request.getQuery() + "\"");
            System.out.println("Model: " + request.getModel());
            System.out.println("Total documents: " + request.getDocuments().size());
            System.out.println("Top documents returned: " + results.size());
            System.out.println("\nTop ranked documents:");
            System.out.println("-".repeat(80));
            
            for (int i = 0; i < results.size(); i++) {
                Rerankv2200ResponseResultsInner result = results.get(i);
                // V2 response only includes index and relevance score (no document text in response)
                System.out.printf("\n%d. Relevance Score: %.4f\n", i + 1, result.getRelevanceScore());
                System.out.printf("   Original Index: %d\n", result.getIndex());
                // Get the original document text from our input array
                System.out.printf("   Text: %s\n", documents.get(result.getIndex()));
            }
            
            System.out.println("-".repeat(80));
            
            // Show API metadata if available
            if (response.getId() != null) {
                System.out.println("\nAPI Response ID: " + response.getId());
            }
            if (response.getMeta() != null && response.getMeta().getApiVersion() != null) {
                System.out.println("API Version: " + response.getMeta().getApiVersion().getVersion());
            }
            if (response.getMeta() != null && response.getMeta().getBilledUnits() != null) {
                System.out.println("Billed Units: " + response.getMeta().getBilledUnits().getSearchUnits());
            }
        } catch (ApiException ex) {
            System.err.println("Rerank request failed: " + ex.getCode() + " " + ex.getMessage());
            if (ex.getResponseBody() != null) {
                System.err.println(ex.getResponseBody());
            }
            System.exit(1);
        }
    }
}
