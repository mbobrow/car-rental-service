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
       | FordTest | FocusTest | SEDAN    | PETROL   | 2020 | false    |
     When I send a valid request with content type "application/json" to the service
     Then The response code is 201
     And The response body has key model and FocusTest value
  #Send GET to car/id with id of the created car
  #Assert some response data is correct (the same as of the car we inserted in the previous step)

























  Scenario: Checking the correctness of the GET query
    Given the REST service with initial "car" data id "102" is available and the "GET" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contains the "brand" "Volkswagen" and the "model" "Golf" and the status code "200"
