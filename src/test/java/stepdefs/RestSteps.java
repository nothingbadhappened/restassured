package stepdefs;

import enums.Endpoint;
import enums.HttpMethod;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import util.Context;
import enums.ContextKey;
import util.JsonUtil;
import util.RestManager;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@Log4j2
@AllArgsConstructor
public class RestSteps {
    private RestManager restManager;

    @Given("the user sets the base API URL to {string}")
    public void theUserSetsTheBaseAPIURL(String baseAPIURL) {
        log.info("Running step: the user sets the base API URL to " + baseAPIURL);
        Context.setContext(ContextKey.BASE_URL, baseAPIURL);
        log.debug("Context updated: [key: " + ContextKey.BASE_URL
                + "value: " + Context.getContextValue(ContextKey.BASE_URL) + "]");
    }

    @And("the user gets the auth token")
    public void theUserGetsTheAuthToken() {
        log.info("Running step: the user gets the auth token");

        restManager.makeRequest(HttpMethod.POST, Endpoint.LOGIN, null,
                JsonUtil.getJsonFileByName("login.json"));
        restManager.saveRequestResponseContext();
        Response response = restManager.getStoredResponse();

        log.debug(" >>>>>>>> Request data:\n" + restManager.getRequestDataString());
        log.debug(" <<<<<<<< Response data:\n" + restManager.getResponseDataString());

        String authtoken = response.jsonPath().getString("token");
        log.debug("Auth token obtained: " + authtoken);

        Context.setContext(ContextKey.AUTH_TOKEN, authtoken);
        log.debug("Context updated: [key: " + ContextKey.AUTH_TOKEN + "]"
                + " value: [" + Context.getContextValue(ContextKey.AUTH_TOKEN) + "]");
    }

    @Given("the user sends the request with the following data:")
    public void theUserSendsTheRequestWithTheFollowingData(Map<String, String> requestData) {
        log.info("Running step: the user sends the request with the following data:" + requestData.toString());
        String identifier = requestData.get("id") == null ? "" : "/" + requestData.get("id");

        if (requestData.get("requestBody") == null) {
            restManager.makeRequest(
                    HttpMethod.valueOf(requestData.get("method")),
                    Endpoint.valueOf(requestData.get("endpoint")),
                    identifier);
        } else if (requestData.get("requestBody").contains(".json")) {
            restManager.makeRequest(
                    HttpMethod.valueOf(requestData.get("method")),
                    Endpoint.valueOf(requestData.get("endpoint")),
                    identifier,
                    JsonUtil.getJsonFileByName(requestData.get("requestBody")));

        }
        restManager.saveRequestResponseContext();
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int responseCode) {
        log.info("Running step: the response status is " + responseCode);
        Response response = (Response) Context.getContextValue(ContextKey.CURRENT_RESPONSE);
        assertThat(response.statusCode()).as("Response status is not matching!").isEqualTo(responseCode);
    }
}
