Feature: API TEST SANDBOX

  Background: The user sets the base API URL
    Given the user sets the base API URL to "https://reqres.in/api"
    And the user gets the auth token

  Scenario Outline: The user is able to fetch the users list
    Given the user sends the request with the following data:
    | method        | <method>        |
    | endpoint      | <endpoint>      |
    | requestBody   | <requestBody>   |
    Then the response status is 200

    Examples:
    | method        |  endpoint       |  requestBody    |
    | GET           |  USERS          |  EMPTY          |
    | POST          |  LOGIN          |  login.json     |