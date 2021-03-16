package com.capgemini.demo.carrental.stepdefs;

import static com.capgemini.demo.carrental.util.ConstantUtils.API_V1;
import static com.capgemini.demo.carrental.util.ConstantUtils.LOCAL_HOST;
import static com.capgemini.demo.carrental.util.ConstantUtils.LOCAL_HOST_PORT;

import com.google.common.collect.ImmutableMap;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

import com.capgemini.demo.carrental.config.StepDefsConfig;
import com.capgemini.demo.carrental.util.RestTemplateUtils;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
@ContextConfiguration(classes = {StepDefsConfig.class})
public class StepDefsImplementation {

    private static final Logger LOGGER = LoggerFactory.getLogger(StepDefsImplementation.class);
    private static final String CAR_SERVICE_ADDRESS = LOCAL_HOST
            .concat(LOCAL_HOST_PORT)
            .concat(API_V1);

    ImmutableMap<String, String> immutableMap = ImmutableMap.<String, String>builder()
            .put("car", "car/")
            .put("cars available", "car?available=true")
            .put("rental", "rental/")
            .put("rental search", "rental/search/")
            .put("rental search by car", "rental/search/byCar/")
            .put("rental search by tenant", "rental/search/byTenant/")
            .put("tenant", "tenant/")
            .put("remove rental", "rental/remove/")
            .build();

    private HttpMethod requestType;
    private String requestAsString;
    private String requestBody;
    private String requestUrl;
    private String responseStatusCode;
    private String responseBody;
    private int id;

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    //---------------Checking the correctness of the GET query
    @Given("the REST service with initial {string} data id {string} is available and the {string} method is supported")
    public void the_rest_service_with_initial_car_data_id_is_available_and_the_method_is_supported(String endpoint, String id, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(immutableMap.get(endpoint)).concat(id);
        requestAsString = "";
    }

    @When("I send request with content type {string} to the service")
    public void i_send_request_with_content_type_to_the_service(String contentType) {
        ResponseEntity<String> response = restTemplateUtils.processHttpRequest(requestType, requestAsString, requestUrl, contentType);
        retrieveResponseBodyAndStatusCode(response);
    }

    @Then("the retrieved body should contains the {string} {string} and the {string} {string} and the status code {string}")
    public void the_retrieved_body_should_contains_the_brand_name_and_the_model_and_the_status_code(String brandKey, String brandName, String modelKey, String modelName, String expectedStatusCode) throws JSONException {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
        JSONObject jsonResponseBody = new JSONObject(responseBody);
        Assert.assertEquals(brandName, jsonResponseBody.get(brandKey).toString());
        Assert.assertEquals(modelName, jsonResponseBody.get(modelKey).toString());
    }

    private void retrieveResponseBodyAndStatusCode(ResponseEntity<String> response) {
        if (response != null) {
            final int responseStatusCodeNumber = response.getStatusCodeValue();
            responseStatusCode = String.valueOf(responseStatusCodeNumber);
            responseBody = response.getBody();
        } else {
            responseStatusCode = restTemplateUtils.getErrorResponseStatusCode();
            responseBody = restTemplateUtils.getErrorResponseBody();
        }
        LOGGER.info("Status code: {}", responseStatusCode);
    }


    @Given("the REST get all {string} service is available and the {string} method is supported")
    public void theRESTGetAllServiceIsAvailableAndTheMethodIsSupported(String endpoint, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(immutableMap.get(endpoint));
        requestAsString = "";

    }

    @Then("the retrieved body should contains the list of cars and the status code {string}")
    public void theRetrievedBodyShouldContainsTheListOfCarsAndTheStatusCode(String expectedStatusCode) throws JSONException {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
        JSONArray jsonResponseBody = new JSONArray(responseBody);
        Assert.assertTrue(jsonResponseBody.length()==21);
    }
}