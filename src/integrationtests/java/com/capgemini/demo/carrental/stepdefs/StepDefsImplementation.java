package com.capgemini.demo.carrental.stepdefs;

import com.capgemini.demo.carrental.model.Car;
import com.capgemini.demo.carrental.model.Rental;
import io.cucumber.java.After;
import io.cucumber.java.Before;
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

    private RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<String> response;
    ResponseEntity<Car[]> responseCar;
    ResponseEntity<Rental> responseRental;

    //---------------Checking the correctness of the GET query
    @Given("the REST service with initial {string} data id {string} is available and the {string} method is supported")
    public void the_rest_service_with_initial_car_data_id_is_available_and_the_method_is_supported(String endpoint, String id, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
//        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint)).concat(id);
        requestUrl = "http://localhost:8080/api/v1/car/".concat(id);
    }

    @When("I send request with content type {string} to the service")
    public void i_send_request_with_content_type_to_the_service(String contentType) {
//        ResponseEntity<String> response = restTemplateUtils.processHttpRequest(requestType, requestBody.toString(), requestUrl, contentType);
//        Map<ResponseElementsEnum, String> responseElements = restTemplateUtils.retrieveResponseBodyAndStatusCode(response);
//        responseStatusCode = responseElements.get(ResponseElementsEnum.RESPONSE_STATUS_CODE);
//        responseBody = responseElements.get(ResponseElementsEnum.RESPONSE_BODY);
        response = restTemplate.getForEntity(requestUrl, String.class);
        System.out.println("");
    }

    @Then("the retrieved body should contains the {string} {string} and the {string} {string} and the status code {string}")
    public void the_retrieved_body_should_contains_the_brand_name_and_the_model_and_the_status_code(String brandKey, String brandName, String modelKey, String modelName, String expectedStatusCode) throws JSONException {
        int actualStatuSoceValue = response.getStatusCodeValue();
        Assert.assertEquals(expectedStatusCode, String.valueOf(actualStatuSoceValue));
        String body = response.getBody();
        JSONObject responseCar = new JSONObject(body);
        Assert.assertEquals(brandName, responseCar.get(brandKey));
        Assert.assertEquals(modelName, responseCar.get(modelKey));
        Assert.assertEquals("2017", responseCar.get("year").toString());
    }
}