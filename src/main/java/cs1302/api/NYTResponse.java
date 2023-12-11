package cs1302.api;

/**
 * Represents a response from the NYT Top Stories API. This is used
 * by GSON to create an object from the JSON response body.
 */
public class NYTResponse {
    String status;
    int num_results;
    NYTResult[] results;
} // NYTResponse
