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




















  Scenario: Checking the correctness of the GET query
    Given the REST service with initial "car" data id "102" is available and the "GET" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contains the "brand" "Volkswagen" and the "model" "Golf" and the status code "200"
