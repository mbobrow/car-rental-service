# Car Rental
The Spring Boot REST micro-service with in-memory H2 data base to simulate simplified car rental.

## Swagger
Here you can find the Swagger documentation.  
Example URL with Swagger UI: http://localhost:8080/swagger-ui/#/

# H2 Data Base
Here you can find H2 console.  
Example URL: http://localhost:8080/h2-console/  
**JDBC URL** can be found in [application.properties](src/main/resources/application.properties) - `spring.datasource.url`  

## Entities
* **Car** - has many to one relation with _Rental_
* **Tenant** - has one to one relation with _Rental_
* **Rental** - has exactly one _tenant_ and at least one _car_. Can be created only by already existed _tenant_ and _car(s)_ in H2 data base. 

## Pre-defined data file
[data.sql](https://github.com/shadarok/car-rental/blob/master/src/main/resources/data.sql) fills _Car_ and _Tenat_ entities in H2 data base.

