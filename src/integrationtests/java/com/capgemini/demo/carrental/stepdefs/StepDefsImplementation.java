package com.capgemini.demo.carrental.stepdefs;

import com.capgemini.demo.carrental.config.StepDefsConfig;
import com.capgemini.demo.carrental.model.Car;
import com.capgemini.demo.carrental.model.Rental;
import com.capgemini.demo.carrental.util.RestTemplateUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.capgemini.demo.carrental.util.ConstantUtils.CAR_SERVICE_ADDRESS;
import static com.capgemini.demo.carrental.util.ConstantUtils.ENDPOINT_SELECTOR;

@CucumberContextConfiguration
@SpringBootTest
@ContextConfiguration(classes = {StepDefsConfig.class})
public class StepDefsImplementation {

    private HttpMethod requestType;
    private JSONObject requestBody;
    private String requestUrl;
    private String responseStatusCode;
    private ResponseEntity<Car> responseEntity;
    private String responseBody;
    private String id;
    Object requestDataModel;

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    @Before
    public void setUp() {
        this.requestBody = new JSONObject();
    }

    @After
    public void cleanUp() {
        responseStatusCode = "";
        responseBody = "";
    }

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Car[]> responseCar;
    ResponseEntity<Rental> responseRental;

    //---------------Checking the correctness of the GET query
    @Given("the REST service with initial {} data id {} is available and the {} method is supported")
    public void prepareEndpointWithData(String endpoint, String id, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint).concat(id));
    }

    @When("the REST service {string} is available with the previously created car id and the {} method is supported")
    public void theRESTServiceIsAvailableWithThePreviouslyCreatedCarIdAndThePOSTMethodIsSupported(String endpoint, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint).concat(responseEntity.getBody().getId()));
    }

    @Given("the REST service {string} is available and the {} method is supported")
    public void prepareEndpoint(String endpoint, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint));
    }

    @And("User prepares request body to create a new car")
    public void userPreparesRequestBodyToCreateANewCar(Car carDataModel) {
        requestDataModel = carDataModel;
    }

    @When("I send request with content type {string} to the service")
    public void sendRequest(String contentType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(contentType));
        HttpEntity<Object> reqEntity = new HttpEntity<>(requestDataModel, httpHeaders);
        responseEntity = restTemplate.exchange(requestUrl, requestType, reqEntity, Car.class);
    }

    @When("I send POST request with content type {string} to the service")
    public void sendPost(String contentType) {
        RequestEntity<Object> requestEntity2 = RequestEntity
                .post(URI.create(requestUrl))
                .contentType(MediaType.valueOf(contentType))
                .body(requestDataModel);
        responseEntity = restTemplate.exchange(requestEntity2, Car.class);
    }


    @DataTableType
    public Car mapDataTable(Map<String, String> entry) {
        Car car = new Car();
        car.setBrand(entry.get("brand"));
        car.setModel(entry.get("model"));
        car.setBodyType(entry.get("bodyType"));
        car.setFuelType(entry.get("fuelType"));
        car.setYear(entry.get("year"));
        car.setIsRented(entry.get("isRented"));
        return car;
    }

    @Then("the retrieved  body should contain the data and the status code {int}")
    public void theRetrievedBodyShouldContainTheAndTheAndTheStatusCode(int expectedStatusCode, Car expectedCarData) {
        //Arrange
        int actualStatusCode = responseEntity.getStatusCodeValue();
        //Assert
        Assert.assertEquals(expectedStatusCode, actualStatusCode);

        //Arrange
        Car actualCarData = responseEntity.getBody();
        //Assert
        Assert.assertEquals(expectedCarData, actualCarData);
    }
}