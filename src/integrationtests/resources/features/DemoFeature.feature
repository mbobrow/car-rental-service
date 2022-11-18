Feature: Checking the correct acting of the Car Rental REST service
  As a user
  I want to validate the execution of the GET, POST, PUT, PATCH and DELETE methods
  By sending queries to the service

  @test
  Scenario: Checking the correctness of the GET query

    Scenario:
    Given the REST service with initial car data id 100 is available and the GET method is supported
      When I send request with content type "application/json" to the service
      Then the retrieved  body should contain the "brand" "Opel" and the "model" "Astra" and the status code 200
        | id  | brand | model | bodyType  | fuelType | year | isRented |
        | 100 | Opel  | Astra | HATCHBACK | DIESEL   | 2017 | false    |



#    {
#    "id": 100,
#    "brand": "Opel",
#    "model": "Astra",
#    "bodyType": "HATCHBACK",
#    "fuelType": "DIESEL",
#    "year": 2017,
#    "isRented": false
#    }