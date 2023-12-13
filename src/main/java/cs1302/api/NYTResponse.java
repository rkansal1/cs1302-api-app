package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a response from the NYT Top Stories API. This is used
 * by GSON to create an object from the JSON response body.
 */
public class NYTResponse {
    String status;

    @SerializedName("num_results")
    int numResults;

    NYTResult[] results;
} // NYTResponse
