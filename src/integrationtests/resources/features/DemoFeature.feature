Feature: Checking the correct acting of the Car Rental REST service
  As a user
  I want to validate the execution of the GET, POST, PUT, PATCH and DELETE methods
  By sending queries to the service

 @test
  Scenario: Send GET request to cars endpoint to get all cars
    Given the REST service with initial "car" endpoint and 102 id is available and the "GET" method is supported
    When I send a valid request with content type "application/json" to the service
    Then The response code is 200
    And The response body has key brand and Volkswagen value

   Scenario: On POST request to car endpoint a new car is created
     Given The REST service with initial "car" endpoint is available and the "POST" method is supported
     And Prepare request body with data
       | brand    | model     | bodyType | fuelType | year | isRented |
       | FordTest | FocusTest | SEDAN    | PETROL   | 1990 | false    |
     When I send a valid request with content type "application/json" to the service
     Then The response code is 201
     And The response body has key model and FocusTest value
     When The REST service with initial "car" endpoint and id of the added resource is available and the "GET" method is supported
     And I send a valid request with content type "application/json" to the service
     Then The response code is 200
     And The response body has key year and 1990 value

  Scenario: On DELETE method to car endpoint a car is removed
    Given The REST service with initial "car" endpoint is available and the "POST" method is supported
    And Prepare request body with data
      | brand    | model     | bodyType | fuelType | year | isRented |
      | FordTest | FocusTest | SEDAN    | PETROL   | 1990 | false    |
    When I send a valid request with content type "application/json" to the service
    Then The response code is 201
    And Save crated car id
    And The response body has key model and FocusTest value
    When The REST service with initial "car" endpoint and id of the added resource is available and the "DELETE" method is supported
    And I send a valid request with content type "application/json" to the service
    Then The response code is 200
    When The REST service with initial "car" endpoint and id of the added resource is available and the "GET" method is supported
#    Exception is thrown because response has an error. To tackle that .exchange method has to be in try catch and in the catch section error code
#    and body has to be extracted from the error.
    And I send a valid request with content type "application/json" to the service
    Then The response code is 404



























  Scenario: Checking the correctness of the GET query
    Given the REST service with initial "car" data id "102" is available and the "GET" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contains the "brand" "Volkswagen" and the "model" "Golf" and the status code "200"
