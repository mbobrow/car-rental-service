Feature: Checking the correct acting of the Car Rental REST service
  As a user
  I want to validate the execution of the GET, POST, PUT, PATCH and DELETE methods
  By sending queries to the service

  @test
  Scenario: Checking the correctness of the GET query

    Scenario:
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
  Scenario: Send Post req to car
    Given the REST service "car" is available and the POST method is supported
    And User prepares request body to create a new car
      | brand | model    | bodyType  | fuelType | year | isRented |
      | Opel  | testdj22 | HATCHBACK | DIESEL   | 2017 | false    |
    When I send POST request with content type "application/json" to the service
    Then the retrieved  body should contain the data and the status code 201
      | brand | model    | bodyType  | fuelType | year | isRented |
      | Opel  | testdj22 | HATCHBACK | DIESEL   | 2017 | false    |








#    When the REST service with initial car data id 100 is available and the GET method is supported
#      And I send request with content type "application/json" to the service
#    Then the retrieved  body should contain the "brand" "Opel" and the "model" "Astra" and the status code 200
#      | brand | model | bodyType  | fuelType | year | isRented |
#      | Opel  | test  | HATCHBACK | DIESEL   | 2017 | false    |



#    {
#    "id": 100,
#    "brand": "Opel",
#    "model": "Astra",
#    "bodyType": "HATCHBACK",
#    "fuelType": "DIESEL",
#    "year": 2017,
#    "isRented": false
#    }