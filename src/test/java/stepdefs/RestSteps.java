package stepdefs;

import util.Context;
import util.ContextKeys;
import util.JsonReader;
import util.RestManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

public class RestSteps {
    @Given("the user sets the base API URL to {string}")
    public void theUserSetsTheBaseAPIURL(String baseAPIURL) {
        System.out.printf("\nRunning step: the user sets the base API URL to [%s]\n", baseAPIURL);
        Context.setContext(ContextKeys.BASE_URL, baseAPIURL);
        System.out.printf("Context updated: [key:%s, value:%s]\n",
                ContextKeys.BASE_URL, Context.getContextValue(ContextKeys.BASE_URL));
    }

    @And("the user gets the auth token")
    public void theUserGetsTheAuthToken() {
        System.out.println("\nRunning step: the user gets the auth token");
        RestManager restManager = new RestManager();

        restManager.makeRequest("POST", "/login",
                JsonReader.getJsonStringFrom("login.json"));
        Response response = restManager.getStoredResponse();

        System.out.println(" >>>>>>>> Request data:");
        restManager.printStoredRequest();
        System.out.println(" <<<<<<<< Response data:");
        restManager.printStoredResponse();

        String authtoken = response.jsonPath().getString("token");
        System.out.printf("Auth token obtained: [%s]\n", authtoken);

        Context.setContext(ContextKeys.CURRENT_RESPONSE, response);
        System.out.printf("Context updated: [key:%s, value:%s]\n",
                ContextKeys.CURRENT_RESPONSE, Context.getContextValue(ContextKeys.CURRENT_RESPONSE));

        Context.setContext(ContextKeys.AUTH_TOKEN, authtoken);
        System.out.printf("Context updated: [key:%s, value:%s]\n",
                ContextKeys.AUTH_TOKEN, Context.getContextValue(ContextKeys.AUTH_TOKEN));
    }

    @Given("the user sends the request with the following data:")
    public void theUserCreatesTheRequestWithTheFollowingData(Map<String, String> requestData) {
        System.out.println("\nRunning step: the user sends the request with the following data:");
        requestData.forEach((k, v) -> System.out.println(k + ": " + v));
        RestManager restManager = new RestManager();
        if(requestData.get("requestBody").equalsIgnoreCase("empty")){
            restManager.makeRequest(
                    requestData.get("method"),
                    requestData.get("endpoint")
            );
            Context.setContext(ContextKeys.CURRENT_REQUEST, restManager.getStoredRequest());
            System.out.printf("Context updated: [key:%s, value:%s]\n",
                    ContextKeys.CURRENT_REQUEST, Context.getContextValue(ContextKeys.CURRENT_REQUEST));

            Context.setContext(ContextKeys.CURRENT_RESPONSE, restManager.getStoredResponse());
            System.out.printf("Context updated: [key:%s, value:%s]\n",
                    ContextKeys.CURRENT_RESPONSE, Context.getContextValue(ContextKeys.CURRENT_RESPONSE));

            System.out.println(" >>>>>>>> Request data:");
            restManager.printStoredRequest();
            System.out.println(" <<<<<<<< Response data:");
            restManager.printStoredResponse();
        } else if (requestData.get("requestBody").contains(".json")) {
            restManager.makeRequest(
                    requestData.get("method"),
                    requestData.get("endpoint"),
                    JsonReader.getJsonStringFrom(requestData.get("requestBody"))
            );
            Context.setContext(ContextKeys.CURRENT_REQUEST, restManager.getStoredRequest());
            System.out.printf("Context updated: [key:%s, value:%s]\n",
                    ContextKeys.CURRENT_REQUEST, Context.getContextValue(ContextKeys.CURRENT_REQUEST));

            Context.setContext(ContextKeys.CURRENT_RESPONSE, restManager.getStoredResponse());
            System.out.printf("Context updated: [key:%s, value:%s]\n",
                    ContextKeys.CURRENT_RESPONSE, Context.getContextValue(ContextKeys.CURRENT_RESPONSE));

            System.out.println(" >>>>>>>> Request data:");
            restManager.printStoredRequest();
            System.out.println(" <<<<<<<< Response data:");
            restManager.printStoredResponse();
        }
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int responseCode) {
        System.out.println("\nRunning step: the response status is " + responseCode);
        Response response = (Response)Context.getContextValue(ContextKeys.CURRENT_RESPONSE);
        Assert.assertEquals(responseCode, response.statusCode());
    }
}
