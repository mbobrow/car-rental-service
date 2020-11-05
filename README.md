# Car Rental
The Spring Boot REST micro-service with in-memory H2 data base to simulate car rental.

# H2 Data Base
## Entities
* Car - has many to one relation with _Rental_
* Tenant - has one to one relation with _Rental_
* Rental - has exactly one _tenant_ and at least one _car_. Can be created only by already existed _tenant_ and _car(s)_ in H2 data base. 

## Pre-defined data file
[data.sql](https://github.com/shadarok/car-rental/blob/master/src/main/resources/data.sql) fills _Car_ and _Tenat_ entities in H2 data base.

