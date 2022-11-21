Feature: Checking the correct acting of the Car Rental REST service
  As a user
  I want to validate the execution of the GET, POST, PUT, PATCH and DELETE methods
  By sending queries to the service


  @test
    Scenario: POST request to car correctly adds a new car
    Given the REST service "car" is available and the POST method is supported
      And User prepares request body to create a new car
        | brand | model    | bodyType  | fuelType | year | isRented |
        | Opel  | testdj22 | HATCHBACK | DIESEL   | 2017 | false    |
      And I send request with content type "application/json" to the service
    Then the retrieved  body should contain the data and the status code 201
      | brand | model    | bodyType  | fuelType | year | isRented |
      | Opel  | testdj22 | HATCHBACK | DIESEL   | 2017 | false    |
    When the REST service "car" is available with the previously created car id and the GET method is supported
      And I send request with content type "application/json" to the service
    Then the retrieved  body should contain the data and the status code 200
        | brand | model    | bodyType  | fuelType | year | isRented |
        | Opel  | testdj22 | HATCHBACK | DIESEL   | 2017 | false    |

  @test
  Scenario: POST request to tenant correctly adds a new tenant
#    TODO test to modify for tenant
    Given the REST service "car" is available and the POST method is supported
    And User prepares request body to create a new car
      | brand | model    | bodyType  | fuelType | year | isRented |
      | Opel  | testdj22 | HATCHBACK | DIESEL   | 2017 | false    |
    When I send POST request with content type "application/json" to the service
    Then the retrieved  body should contain the data and the status code 201
      | brand | model    | bodyType  | fuelType | year | isRented |
      | Opel  | testdj22 | HATCHBACK | DIESEL   | 2017 | false    |