package cs1302.api;

/**
 * Represents a result in a response from the NYT Top Stories API. This is
 * used by Gson to create an object from the JSON response body.
 */
public class NYTResult {
    String title;
    String[] des_facet;
} // NYTResult
