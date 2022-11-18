package com.capgemini.demo.carrental.stepdefs;

import static com.capgemini.demo.carrental.util.ConstantUtils.CAR_SERVICE_ADDRESS;
import static com.capgemini.demo.carrental.util.ConstantUtils.ENDPOINT_SELECTOR;

import java.util.List;
import java.util.Map;

import com.capgemini.demo.carrental.model.Car;
import com.capgemini.demo.carrental.model.Rental;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

import com.capgemini.demo.carrental.config.StepDefsConfig;
import com.capgemini.demo.carrental.util.ResponseElementsEnum;
import com.capgemini.demo.carrental.util.RestTemplateUtils;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

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
    private Integer id;

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
    public void prepareEndpoint(String endpoint, String id, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint).concat(id));
    }

    @When("I send request with content type {string} to the service")
    public void sendRequest(String contentType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(contentType));
        HttpEntity<String> reqEntity = new HttpEntity<>(httpHeaders);
        responseEntity = restTemplate.exchange(requestUrl, requestType, reqEntity, Car.class);
    }


    @DataTableType
    private Car mapDataTable(DataTable dataTable) {
        List<Map<String, String>> dataTableMap = dataTable.asMaps(String.class, String.class);
        Car car = new Car();
        car.setId(dataTableMap.get(0).get("id"));
        car.setId(dataTableMap.get(0).get("brand"));
        car.setId(dataTableMap.get(0).get("model"));
        car.setId(dataTableMap.get(0).get("bodyType"));
        car.setId(dataTableMap.get(0).get("fuelType"));
        car.setId(dataTableMap.get(0).get("year"));
        car.setId(dataTableMap.get(0).get("isRented"));
        return car;
    }

    @Then("the retrieved  body should contain the {string} {string} and the {string} {string} and the status code {int}")
    public void theRetrievedBodyShouldContainTheAndTheAndTheStatusCode(String brandKey, String brandName, String modelKey, String modelName, int expectedStatusCode, Car carData) throws JSONException {
        //Arrange
        int actualStatusCode = responseEntity.getStatusCodeValue();
        //Assert
        Assert.assertEquals(expectedStatusCode, actualStatusCode);


        //Arrange
        //Assert
        Assert.assertEquals(brandName, responseEntity.getBody().getBrand());
        Assert.assertEquals(modelName, responseEntity.getBody().getModel());
        Assert.assertEquals(carData, responseEntity.getBody());
    }


}