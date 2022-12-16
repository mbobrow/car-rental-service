Feature: Checking the correct acting of the Car Rental REST service
  As a user
  I want to validate the execution of the GET, POST, PUT, PATCH and DELETE methods
  By sending queries to the service

 @test
  Scenario: Send GET request to cars endpoint to get all cars
    Given the REST service with initial "car" endpoint is available and the "GET" method is supported
    When I send a valid request with content type "application/json" to the service
    Then The response code is 200
      And The response length is bigger than 1


@test
  Scenario: Send POST request to cars endpoint to add a new car and then delete it
    Given the REST service with initial "car" endpoint is available and the "POST" method is supported
      And User prepares a car request body with data using dataModel
        | brand | model | bodyType  | fuelType | year | isRented |
        | Opel  | Astra | HATCHBACK | DIESEL   | 2017 | false    |
    When I send a valid request with content type "application/json" to the service
    Then The response code is 201
      And Response should have correct data using dataModel


  @test
  Scenario Outline: Send <method> request to the cars endpoint to <method> the created car
    Given the REST service with initial "car" endpoint is available and the "POST" method is supported
      And User prepares a car request body with data
        | brand | model | bodyType  | fuelType | year | isRented |
        | Opel  | Astra | HATCHBACK | DIESEL   | 2017 | false    |
    When I send a valid request with content type "application/json" to the service
    Then The response code is 201
      And The car endpoint response has correct data
    When the REST service with initial "car" endpoint and created car id is available and the "<method>" method is supported
      And I send a valid request with content type "application/json" to the service
    Then The response code is 200
      And The car endpoint response has correct data

    Examples:
      | method |
      | DELETE |
      | GET    |











  Scenario: Checking the correctness of the GET query
    Given the REST service with initial "car" data id "102" is available and the "GET" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contains the "brand" "Volkswagen" and the "model" "Golf" and the status code "200"
