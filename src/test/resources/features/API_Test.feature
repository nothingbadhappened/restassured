Feature: API TEST SANDBOX

  Background: The user sets the base API URL
    Given the user gets the auth token

  Scenario Outline: The user is able to perform request and validate response code
    Given the user sends the request with the following data:
    | method        | <method>        |
    | endpoint      | <endpoint>      |
    | id            | <id>            |
    | requestBody   | <requestBody>   |
    Then the response status is <responseCode>

    Examples:
    # "id" and "requestBody" are optional
    | method        |  endpoint   | id    |  requestBody    | responseCode |
    | POST          |  LOGIN      |       |  login.json     | 200          |
    | GET           |  USERS      | 2     |                 | 200          |
    | GET           |  USERS      | 23    |                 | 404          |

    Scenario: The user can fetch specific user details
      Given the user calls get user endpoint for user id 2
      Then user data is returned
      And the response status is 200