package com.capgemini.demo.carrental.stepdefs;

import static com.capgemini.demo.carrental.util.ConstantUtils.CAR_SERVICE_ADDRESS;
import static com.capgemini.demo.carrental.util.ConstantUtils.ENDPOINT_SELECTOR;

import java.util.Arrays;
import java.util.Map;

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
    private ResponseEntity<String> response;
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

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Car[]> responseCar;
    ResponseEntity<Rental> responseRental;

    //---------------Checking the correctness of the GET query
    @Given("the REST service with initial {string} data id {string} is available and the {string} method is supported")
    public void the_rest_service_with_initial_car_data_id_is_available_and_the_method_is_supported(String endpoint, String id, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint)).concat(id);
//        requestType = HttpMethod.valueOf(httpMethod);
//        requestUrl = "http://localhost:8080/api/v1/".concat(endpoint).concat("/").concat(id);
    }

    @When("I send request with content type {string} to the service")
    public void i_send_request_with_content_type_to_the_service(String contentType) {
//        ResponseEntity<String> response = restTemplateUtils.processHttpRequest(requestType, requestBody.toString(), requestUrl, contentType);
//        Map<ResponseElementsEnum, String> responseElements = restTemplateUtils.retrieveResponseBodyAndStatusCode(response);
//        responseStatusCode = responseElements.get(ResponseElementsEnum.RESPONSE_STATUS_CODE);
//        responseBody = responseElements.get(ResponseElementsEnum.RESPONSE_BODY);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.valueOf(contentType));
        HttpEntity<String> requestEntity = new HttpEntity<>("",requestHeaders);
//        response = restTemplate.exchange(requestUrl, requestType, requestEntity, String.class);
        response = restTemplate.getForEntity(requestUrl, String.class);
        System.out.println("");
    }

    @Then("the retrieved body should contains the {string} {string} and the {string} {string} and the status code {string}")
    public void the_retrieved_body_should_contains_the_brand_name_and_the_model_and_the_status_code(String brandKey, String brandName, String modelKey, String modelName, String expectedStatusCode) throws JSONException {
//        Assert.assertEquals(expectedStatusCode, responseStatusCode);
//        JSONObject jsonResponseBody = new JSONObject(responseBody);
//        Assert.assertEquals(brandName, jsonResponseBody.get(brandKey).toString());
//        Assert.assertEquals(modelName, jsonResponseBody.get(modelKey).toString());

        String statusCode = String.valueOf(response.getStatusCodeValue());
        Assert.assertEquals(expectedStatusCode, statusCode);

        JSONObject responseObject = new JSONObject(response.getBody());
        String brand = responseObject.getString(brandKey);
        Assert.assertEquals(brandName, responseObject.getString(brandKey));
        Assert.assertEquals(modelName, responseObject.getString(modelKey));
        System.out.println("");
    }
}