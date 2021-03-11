package com.capgemini.demo.carrental.stepdefs;

import com.capgemini.demo.carrental.config.StepDefsConfig;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
@ContextConfiguration(classes = {StepDefsConfig.class})
public class StepDefsImplementation {

    private String serviceAddress = "http://localhost:8080/api/v1/car/";
    private HttpMethod requestType;
    private String requestAsString;
    private String requestUrl;
    private String responseStatusCode;
    private String responseBody;

    Logger log = LoggerFactory.getLogger(StepDefsImplementation.class);

    @Autowired
    private CommonUtility commonUtility;

    @Given("the REST service with initial car data id {string} is available and the {string} method is supported")
    public void the_rest_service_with_initial_car_data_id_is_available_and_the_method_is_supported(String id, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = serviceAddress.concat(id);
        requestAsString = "";
    }

    @When("I send request with content type {string} to the service")
    public void i_send_request_with_content_type_to_the_service(String contentType) {
        ResponseEntity<String> response = commonUtility.processHttpRequest(requestType, requestAsString, requestUrl, contentType);
        retrieveResponseBodyAndStatusCode(response);
    }

    @Then("the retrieved body should contains the brandName {string} and the model {string} and the status code {string}")
    public void the_retrieved_body_should_contains_the_brand_name_and_the_model_and_the_status_code(String firstName, String lastName, String expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
        Assert.assertTrue(responseBody.contains(firstName));
        Assert.assertTrue(responseBody.contains(lastName));
    }

    private void retrieveResponseBodyAndStatusCode(ResponseEntity<String> response) {
        if (response != null) {
            int responseStatusCodeNumber = response.getStatusCodeValue();
            responseStatusCode = "" + responseStatusCodeNumber;
            responseBody = response.getBody();
            log.info("Status code: " + responseStatusCode);
        } else {
            responseStatusCode = commonUtility.getErrorResponseStatusCode();
            responseBody = commonUtility.getErrorResponseBody();
            log.info("Status code: " + responseStatusCode);
        }
    }
}
