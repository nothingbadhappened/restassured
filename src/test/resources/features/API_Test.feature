Feature: API TEST SANDBOX

  Background: The user sets the base API URL
    Given the user sets the base API URL to "https://reqres.in/api"
    And the user gets the auth token

  Scenario Outline: The user is able to perform request and validate response code
    Given the user sends the request with the following data:
    | method        | <method>        |
    | endpoint      | <endpoint>      |
    | requestBody   | <requestBody>   |
    Then the response status is <responseCode>

    Examples:
    | method        |  endpoint       |  requestBody    | responseCode |
    | GET           |  USERS          |  EMPTY          | 200          |
    | GET           |  users/23       |  EMPTY          | 404          |
    | POST          |  LOGIN          |  login.json     | 200          |