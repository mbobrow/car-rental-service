package com.capgemini.demo.carrental.stepdefs;

import static com.capgemini.demo.carrental.util.ConstantUtils.CAR_SERVICE_ADDRESS;
import static com.capgemini.demo.carrental.util.ConstantUtils.ENDPOINT_SELECTOR;

import java.util.Arrays;
import com.capgemini.demo.carrental.model.Car;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

import com.capgemini.demo.carrental.config.StepDefsConfig;

import org.junit.Assert;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

@CucumberContextConfiguration
@SpringBootTest
@ContextConfiguration(classes = {StepDefsConfig.class})
public class StepDefsImplementation {

    private HttpMethod requestType;
    private String requestUrl;
    private String requestUrlForSet;
    private ResponseEntity<Car> responseCar;
    private ResponseEntity<Car[]> responseCarSet;

    private RestTemplate restTemplate = new RestTemplate();

    @Before
    public void setUp() {
    }

    @After
    public void cleanUp() {
    }

    //---------------Checking the correctness of the GET query
    @Given("the REST service with initial {string} data id {string} is available and the {string} method is supported")
    public void the_rest_service_with_initial_car_data_id_is_available_and_the_method_is_supported(String endpoint, String id, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint)).concat(id);
        requestUrlForSet = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint));
    }

    @When("I send request with content type {string} to the service")
    public void i_send_request_with_content_type_to_the_service(String contentType) {
        responseCar = restTemplate.getForEntity(requestUrl, Car.class);
        responseCarSet = restTemplate.getForEntity(requestUrlForSet, Car[].class);
    }

    @Then("the retrieved body should contains the {string} {string} and the {string} {string} and the status code {string}")
    public void the_retrieved_body_should_contains_the_brand_name_and_the_model_and_the_status_code(String brandKey, String brandName, String modelKey, String modelName, String expectedStatusCode) throws JSONException {
        //car response
        String statusCode = String.valueOf(responseCar.getStatusCodeValue());
        Assert.assertEquals(expectedStatusCode, statusCode);

        Car car = responseCar.getBody();
        Assert.assertEquals(brandName, car.getBrand());
        Assert.assertEquals(modelName, car.getModel());

        //cars set response
        String statusCodeCars = String.valueOf(responseCar.getStatusCodeValue());
        Assert.assertEquals(expectedStatusCode, statusCodeCars);

        Car[] cars = responseCarSet.getBody();
        Car anyCar = Arrays.stream(cars).filter(i -> i.getBrand().equals("Ford")).findAny().orElse(null);
        Assert.assertEquals("Ford", anyCar.getBrand());
    }
}