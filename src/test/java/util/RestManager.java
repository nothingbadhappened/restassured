package util;

import enums.ContextKey;
import enums.Endpoint;
import enums.HttpMethod;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import lombok.extern.log4j.Log4j2;
import models.userDto.UserDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

@Log4j2
public class RestManager {
    private static RequestSpecification requestSpecification;
    private static Response response;


    public String getRequestDataString() {
        QueryableRequestSpecification queryable = SpecificationQuerier.query(getStoredRequest());
        // TO DO: Debug and refactor...
        try {
            return queryable.getHeaders().toString() + queryable.getBody().toString();
        } catch (NullPointerException e) {
            log.warn(Arrays.toString(e.getStackTrace()));
            return "Failed to extract Request values: " +
                    "\n[queryable.getHeaders().toString() + queryable.getBody().toString()] returned null";
        }
    }

    public String getResponseDataString() {
        return response.getStatusCode()
                + " " + response.getStatusLine()
                + " \n Response Body => "
                + response.getBody().prettyPrint();
    }

    public RequestSpecification getStoredRequest() {
        if (requestSpecification != null) {
            return requestSpecification;
        } else {
            log.error("No request present at the moment, please make sure you create the request first...");
            return null;
        }
    }

    public Response getStoredResponse() {
        if (response != null) {
            return response;
        } else {
            log.error("No response data present at the moment, please make sure you did the request first...");
            return null;
        }
    }

    public void makeRequest(HttpMethod method, Endpoint endpoint, String identifier, File jsonFile) {
        setAuthToken();

        try {
            log.debug("Performing " + method + " request to " + endpoint.value + identifier
                    + " endpoint with the following json body: "
                    + new String(Files.readAllBytes(Paths.get(String.valueOf(jsonFile)))));
        } catch (IOException ignored) {
        }

        requestSpecification = given()
                .header("Content-Type", "application/json")
                .header("Authorization", Context.getContextValue(ContextKey.AUTH_TOKEN).toString())
                .baseUri(Endpoint.BASE_URL.value)
                .basePath(String.valueOf(endpoint.value))
                .body(jsonFile);
        response = getResponseToRequestWith(method, jsonFile);
    }

    public void makeRequest(HttpMethod method, Endpoint endpoint, String identifier) {
        setAuthToken();
        log.debug("Performing " + method + " request to " + endpoint.value + identifier + " endpoint");

        requestSpecification = given()
                .header("Content-Type", "application/json")
                .header("Authorization", Context.getContextValue(ContextKey.AUTH_TOKEN).toString())
                .baseUri(Endpoint.BASE_URL.value)
                .basePath(endpoint.value + identifier);
        response = getResponseToRequestWith(method);
    }

    private Response getResponseToRequestWith(HttpMethod httpMethod) {
        switch (httpMethod) {
            case GET:
                return requestSpecification.when().get()
                        .then().extract().response();
            case DELETE:
                return requestSpecification.when().delete()
                        .then().contentType(ContentType.JSON).extract().response();
            default:
                log.error("Unknown HTTP method [" + httpMethod + "], please check!");
                return null;
        }
    }

    private Response getResponseToRequestWith(HttpMethod httpMethod, File json) {
        switch (httpMethod) {
            case PUT:
                return requestSpecification
                        .body(json)
                        .when()
                        .put()
                        .then().contentType(ContentType.JSON).extract().response();
            case POST:
                return requestSpecification
                        .body(json)
                        .when()
                        .post()
                        .then().contentType(ContentType.JSON).extract().response();
            case PATCH:
                return requestSpecification
                        .body(json)
                        .when()
                        .patch()
                        .then().contentType(ContentType.JSON).extract().response();
            default:
                log.error("Unknown HTTP method [" + httpMethod + "], please check!");
                return null;
        }
    }

    public void saveRequestResponseContext() {
        Context.setContext(ContextKey.CURRENT_REQUEST, getStoredRequest());
        log.debug("Context updated: [key: " + ContextKey.CURRENT_REQUEST + "]"
                + ", value: [" + Context.getContextValue(ContextKey.CURRENT_REQUEST) + "]");
        log.debug(" >>>>>>>> Request data:");
        getRequestDataString();

        Context.setContext(ContextKey.CURRENT_RESPONSE, getStoredResponse());
        log.debug("Context updated: [key: " + ContextKey.CURRENT_RESPONSE + "]"
                + ", value: [" + Context.getContextValue(ContextKey.CURRENT_RESPONSE) + "]");
        log.debug(" <<<<<<<< Response data:");
        getResponseDataString();
    }

    private void setAuthToken() {
        if (Context.getContextValue(ContextKey.AUTH_TOKEN) == null) {
            Context.setContext(ContextKey.AUTH_TOKEN, "DEFAULT_TOKEN");
        }
    }

    public UserDTO getUser(long id) {
        return requestSpecification.when()
                .get()
                .as(UserDTO.class);
    }

    public List<UserDTO> getUsers(long id) {
        return requestSpecification.when()
                .get()
                .as(new TypeRef<List<UserDTO>>() {
                });
    }
}
