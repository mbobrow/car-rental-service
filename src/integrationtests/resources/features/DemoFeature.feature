Feature: Checking the correct acting of the Car Rental REST service
  As a user
  I want to validate the execution of the GET, POST, PUT, PATCH and DELETE methods
  By sending queries to the service

  @test
  Scenario: Checking the correctness of the GET query
    Given the REST service with initial "car" data id "102" is available and the "GET" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contains the "brand" "Volkswagen" and the "model" "Golf" and the status code "200"

  @test
  Scenario Outline: Checking the correctness of the POST query
    Given for the REST service for endpoint we have "<brand>" "<model>" "<bodyType>" "<fuelType>" <year> for the "POST" method
    When I send request with body and content type "application/json" to the service
    Then the retrieved body should contains the id and the status code 201
    Examples:
      | brand | model  | bodyType  | fuelType | year |
      | Dacia | Duster | SUV       | DIESEL   | 2010 |
      | Fiat  | Panda  | HATCHBACK | DIESEL   | 2010 |
