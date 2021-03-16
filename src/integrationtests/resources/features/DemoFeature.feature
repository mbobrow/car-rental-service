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

