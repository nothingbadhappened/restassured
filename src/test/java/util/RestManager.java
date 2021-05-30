package util;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;

import static io.restassured.RestAssured.given;

public class RestManager {
    private RequestSpecification requestSpecification;
    private Response response;


    public void printStoredRequest(){
        QueryableRequestSpecification queryable = SpecificationQuerier.query(requestSpecification);
        System.out.println(queryable.getHeaders());
        System.out.println((String) queryable.getBody());
    }

    public void printStoredResponse(){
        System.out.println(response.getStatusCode() + " " + response.getStatusLine() + " \n Response Body => " + response.getBody().prettyPrint());
    }

    public RequestSpecification getStoredRequest(){
        if(requestSpecification!=null) {
            return requestSpecification;
        } else {
            System.out.println("No request present at the moment, please make sure you create the request first...");
            return null;
        }
    }

    public Response getStoredResponse(){
        if(response!=null) {
            return response;
        } else {
            System.out.println("No response data present at the moment, please make sure you did the request first...");
            return null;
        }
    }

    public void makeRequest(String method, String endpoint, String jsonString){
        endpoint = endpoint.toUpperCase();
        setAuthToken();

        System.out.println("Performing request with the following json body: " + jsonString);
        requestSpecification = given()
                .header("Content-Type", "application/json")
                .header("Authorization", Context.getContextValue(ContextKeys.AUTH_TOKEN).toString())
                .baseUri(Context.getContextValue(ContextKeys.BASE_URL).toString())
                .body(jsonString);
        response = sendRequestAndGetResponse(method, endpoint);
    }

    public void makeRequest(String method, String endpoint){
        endpoint = endpoint.toUpperCase();
        setAuthToken();

        requestSpecification = given()
                .header("Authorization", Context.getContextValue(ContextKeys.AUTH_TOKEN).toString())
                .baseUri(Context.getContextValue(ContextKeys.BASE_URL).toString());
        response = sendRequestAndGetResponse(method, endpoint);
    }

    private Response sendRequestAndGetResponse(String method, String endpoint){
        switch (method.toUpperCase()) {
            case "GET":
                return requestSpecification.when().get(endpoint)
                        .then().contentType(ContentType.JSON).extract().response();
            case "POST":
                return requestSpecification.when().post(endpoint)
                        .then().contentType(ContentType.JSON).extract().response();
            default:
                System.out.printf("Unknown HTTP method [%s], please check", method);
                return null;
        }
    }

    private void setAuthToken(){
        if (Context.getContextValue(ContextKeys.AUTH_TOKEN)==null){
            Context.setContext(ContextKeys.AUTH_TOKEN, "DEFAULT_TOKEN");
        }
    }
}
