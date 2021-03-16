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
  Scenario: Query for all cars
    Given the REST get all "car" service is available and the "GET" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contains the list of cars and the status code "200"

  @test
  Scenario Outline: Query for car id "<id>"
    Given the REST service with initial "car" data id "<id>" is available and the "GET" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contains the "brand" "<brand>" and the "model" "<model>" and the status code "200"
    Examples:
      | id  | brand      | model |
      | 101 | Ford       | Focus |
      | 102 | Volkswagen | Golf  |
      | 103 | Renault    | Clio  |

  @test
  Scenario Outline: Query for tenant id "<id>"
    Given the REST service with initial "tenant" data id "<id>" is available and the "GET" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contains the "name" "<name>" and the "age" "<age>" and the status code "200"

    Examples:
      | id  | name      | age |
      | 100 | Adam      | 30  |
      | 101 | Marek     | 39  |
      | 102 | Katarzyna | 29  |

  @test
  Scenario: Add car and remove it
    Given the REST service with "car" brand "Dacia", model "Duster", body type "SUV", fuel type "PETROL" and year of production 2010 is available and the "POST" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contain the "id" of the 'added' 'car' and the status code "201"
    Given the REST service with previously created "car" id is available and the "DELETE" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contain the "id" of the 'removed' 'car' and the status code "200"

  @test
  Scenario: Remove the car that does not exist
    Given the REST service with initial "car" data id "999" is available and the "DELETE" method is supported
    When I send request with content type "application/json" to the service
    Then the response status code "404"

#  Zadanie domowe

  @test
  Scenario: Change data of the car
    Given the REST service with "car" id "100", brand "Dacia", model "Duster", body type "SUV", fuel type "PETROL" and year of production "2010" is available and the "PUT" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contains the "brand" "Dacia" and the "model" "Duster" and the status code "200"

  @test
  Scenario: Incorrect car id PUT request
    Given the REST service with "car" id "999", brand "Dacia", model "Duster", body type "SUV", fuel type "PETROL" and year of production "2010" is available and the "PUT" method is supported
    When I send request with content type "application/json" to the service
    Then the response status code "404"

  @test
  Scenario: Add a car to non-existing rental
    Given the REST service for "rental" with beginning date "2022-03-09", end date "2022-04-09", car id "101" and tenant id "100" is available and the "PUT" method is supported
    When I send request with content type "application/json" to the service
    Then the response status code "404"

  @test
  Scenario: Cancel non-existing rental by Id
    Given the REST service with initial "remove rental" data id "999" is available and the "DELETE" method is supported
    When I send request with content type "application/json" to the service
    Then the response status code "404"

  @test
  Scenario: Search for non-existing rental by car Id
    Given the REST service with initial "rental search by car" data id "101" is available and the "GET" method is supported
    When I send request with content type "application/json" to the service
    Then the response status code "404"

  @test
  Scenario: Create new rental
    Given the REST service for "rental" with beginning date "2022-03-09", end date "2022-04-09", car id "101" and tenant id "100" is available and the "POST" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contain the "id" of the 'added' 'rental' and the status code "201"
    Given the REST service with initial "rental search by car" data id "101" is available and the "GET" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved rental body should contain the "brand" "Ford" and the "model" "Focus" and the status code "200"

  @test
  Scenario: Add new car to existing rental
    Given the REST service for "rental" with beginning date "2022-03-09", end date "2022-04-09", car id "101" and tenant id "100" is available and the "POST" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contain the "id" of the 'added' 'rental' and the status code "201"
    Given the REST service for "rental" with beginning date "2022-03-09", end date "2022-04-09", car id "102" and tenant id "100" is available and the "PUT" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved body should contain the "id" of the 'added' 'rental' and the status code "200"
    Given the REST service with initial "rental search by tenant" data id "100" is available and the "GET" method is supported
    When I send request with content type "application/json" to the service
    Then the retrieved rental body should contain the "brand" "Volkswagen" and the "model" "Golf" and the status code "200"