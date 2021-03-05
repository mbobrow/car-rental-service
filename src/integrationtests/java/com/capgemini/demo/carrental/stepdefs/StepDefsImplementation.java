package com.capgemini.demo.carrental.stepdefs;

import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@CucumberContextConfiguration
public class StepDefsImplementation {

    private String serviceAddress = "http://localhost:8080/api/v1/car/";
    private HttpMethod requestType;
    private String requestAsString;
    private String requestUrl;
    private String responseStatusCode;
    private String responseBody;

    Logger log = LoggerFactory.getLogger(StepDefsImplementation.class);

    @Autowired
    private CommonUtility commonUtility = new CommonUtility();

//    @Given("^the REST service with initial car data id \"([^\"]*)\" is available and the \"([^\"]*)\" method is supported$")
    public void the_REST_service_with_initial_car_data_id_is_available_and_the_method_is_supported(String id, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = serviceAddress.concat(id);
        requestAsString = "";
    }

//    @When("^I send request with content type \"([^\"]*)\" to the service$")
    public void I_send_request_to_the_service(String contentType) {
        ResponseEntity<String> response = commonUtility.processHttpRequest(requestType, requestAsString, requestUrl, contentType);
        retrieveResponseBodyAndStatusCode(response);
    }

//    @Then("^the retrieved body should contains the brandName \"([^\"]*)\" and the model \"([^\"]*)\" and the status code \"([^\"]*)\"$")
    public void the_retrieved_body_should_match_with_the_status_code(String firstName, String lastName, String expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
        Assert.assertTrue(responseBody.contains(firstName));
        Assert.assertTrue(responseBody.contains(lastName));
    }

//    @Given("^the REST service is available and the \"([^\"]*)\" method is supported$")
    public void the_REST_service_is_available_and_the_method_is_supported(String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = serviceAddress;
    }

//    @When("^I send \"([^\"]*)\" as a command body with content type \"([^\"]*)\" to the service$")
    public void I_send_as_a_command_body_to_the_service(String fileName, String contentType) throws Throwable {
        requestAsString = "{\"firstName\": \"George\",\"lastName\": \"Harrison\",\"role\": \"teacher\"}";

        ResponseEntity<String> response = commonUtility.processHttpRequest(requestType, requestAsString, requestUrl, contentType);
        retrieveResponseBodyAndStatusCode(response);
    }

//    @Then("^the retrieved status code is \"([^\"]*)\"$")
    public void the_retrieved_status_code_is(String expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
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