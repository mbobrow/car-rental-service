package com.capgemini.demo.carrental.stepdefs;

import com.capgemini.demo.carrental.config.StepDefsConfig;
import com.capgemini.demo.carrental.model.Car;
import com.capgemini.demo.carrental.model.Rental;
import com.capgemini.demo.carrental.util.ResponseElementsEnum;
import com.capgemini.demo.carrental.util.RestTemplateUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

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
    private String responseBody;
    private Integer carId;
    JSONObject request = new JSONObject();

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
    ResponseEntity<String> responseEntity;


    @Given("the REST service with initial {string} endpoint and {} id is available and the {string} method is supported")
    public void prepareEndpoint(String endpoint, String id, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint) + id);
    }

    @When("I send a valid request with content type {string} to the service")
    public void sendRequest(String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(contentType));
        HttpEntity<Object> requestEntity = new HttpEntity<>(request.toString(), headers);
        responseEntity = restTemplate.exchange(requestUrl, requestType, requestEntity, String.class);
    }

    @Then("The response code is {int}")
    public void theResponseCodeIs(int expectedResponseCode) {
        //Act
        int actualStatusCode = responseEntity.getStatusCodeValue();
        //Assert
        Assert.assertEquals(expectedResponseCode, actualStatusCode);
    }

    @Then("The response body has key {} and {} value")
    public void responseHasKeyAndValue(String expectedKey, String expectedValue) throws JSONException {
        //Arrange

        //Act
        String actualValue = new JSONObject(responseEntity.getBody())
                .get(expectedKey).toString();
        //Assert
        Assert.assertEquals(expectedValue, actualValue);
    }


    //---------------Checking the correctness of the GET query
    @Given("the REST service with initial {string} data id {string} is available and the {string} method is supported")
    public void the_rest_service_with_initial_car_data_id_is_available_and_the_method_is_supported(String endpoint, String id, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint)).concat(id);
    }

    @When("I send request with content type {string} to the service")
    public void i_send_request_with_content_type_to_the_service(String contentType) {
        ResponseEntity<String> response = restTemplateUtils.processHttpRequest(requestType, requestBody.toString(), requestUrl, contentType);
        Map<ResponseElementsEnum, String> responseElements = restTemplateUtils.retrieveResponseBodyAndStatusCode(response);
        responseStatusCode = responseElements.get(ResponseElementsEnum.RESPONSE_STATUS_CODE);
        responseBody = responseElements.get(ResponseElementsEnum.RESPONSE_BODY);
    }

    @Then("the retrieved body should contains the {string} {string} and the {string} {string} and the status code {string}")
    public void the_retrieved_body_should_contains_the_brand_name_and_the_model_and_the_status_code(String brandKey, String brandName, String modelKey, String modelName, String expectedStatusCode) throws JSONException {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
        JSONObject jsonResponseBody = new JSONObject(responseBody);
        Assert.assertEquals(brandName, jsonResponseBody.get(brandKey).toString());
        Assert.assertEquals(modelName, jsonResponseBody.get(modelKey).toString());
    }

    @Given("The REST service with initial {string} endpoint is available and the {string} method is supported")
    public void prepareCarPostEndpoint(String endpoint, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint));
    }

    @And("Prepare request body with data")
    public void prepareRequestBodyWithData(DataTable table) {
        Map<String, String> requestAsMap = table.asMaps().get(0);
        request = new JSONObject(requestAsMap);
    }
}