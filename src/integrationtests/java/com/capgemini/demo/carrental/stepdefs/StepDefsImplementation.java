package com.capgemini.demo.carrental.stepdefs;

import static com.capgemini.demo.carrental.util.ConstantUtils.CAR_SERVICE_ADDRESS;
import static com.capgemini.demo.carrental.util.ConstantUtils.ENDPOINT_SELECTOR;

import java.util.Map;

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

    @Given("the REST get all {string} service is available and the {string} method is supported")
    public void theRESTGetAllServiceIsAvailableAndTheMethodIsSupported(String endpoint, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint));
    }

    @Then("the retrieved body should contains the list of cars and the status code {string}")
    public void theRetrievedBodyShouldContainsTheListOfCarsAndTheStatusCode(String expectedStatusCode) throws JSONException {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
        JSONArray jsonResponseBody = new JSONArray(responseBody);
        Assert.assertEquals(21, jsonResponseBody.length());
    }
    //---------------Add car and remove it
    @Given("the REST service with {string} brand {string}, model {string}, body type {string}, fuel type {string} and year of production {int} is available and the {string} method is supported")
    public void theRESTServiceWithCarBrandModelBodyTypeFuelTypeAndYearOfProductionIsAvailableAndTheMethodIsSupported(String endpoint, String brand, String model, String type, String fuel, int year, String httpMethod)
            throws JSONException {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint));
        requestBody.put("bodyType", type);
        requestBody.put("brand", brand);
        requestBody.put("fuelType", fuel);
        requestBody.put("model", model);
        requestBody.put("year", year);
    }

    @Then("the retrieved body should contain the {string} of the {string} {string} and the status code {string}")
    public void theRetrievedBodyShouldContainTheOfTheAddedCarAndTheStatusCode(String fieldKey, String operationType, String endpoint, String expectedStatusCode) throws JSONException {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
        JSONObject jsonResponseBody = new JSONObject(responseBody);
        Assert.assertTrue("Response body does not contain \"id\" property", jsonResponseBody.has("id"));
        id = (Integer) jsonResponseBody.get(fieldKey);
        Assert.assertNotNull(id);
    }

    @Given("the REST service with previously created {string} id is available and the {string} method is supported")
    public void theRESTServiceWithPreviouslyCreatedIdIsAvailableAndTheMethodIsSupported(String endpoint, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint)).concat(Integer.toString(id));
    }
    //---------------Remove the car that does not exist
    @Then("the response status code {string}")
    public void theResponseStatusCode(String expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
    }
    //---------------Change year of the car
    @Given("the REST service with {string} id {string}, brand {string}, model {string}, body type {string}, fuel type {string} and year of production {string} is available and the {string} method is supported")
    public void theRESTServiceWithCarIdCarBrandModelBodyTypeFuelTypeAndYearOfProductionIsAvailableAndTheMethodIsSupported(String endpoint, String id, String brand, String model, String type, String fuel, String year, String httpMethod)
            throws JSONException {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint)).concat(id);
        requestBody.put("bodyType", type);
        requestBody.put("brand", brand);
        requestBody.put("fuelType", fuel);
        requestBody.put("model", model);
        requestBody.put("year", year);
    }
    //---------------Add a car to non-existing rental
    @Given("the REST service for {string} with beginning date {string}, end date {string}, car id {string} and tenant id {string} is available and the {string} method is supported")
    public void theRESTServiceForWithBeginningDateEndDateCarIdAndTenantIdIsAvailableAndTheMethodIsSupported(String endpoint, String beginningOfRental, String endOfRental, String carId, String tenantId, String httpMethod)
            throws JSONException {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint));
        requestBody.put("beginningOfRental", beginningOfRental);
        requestBody.put("carIds", new JSONArray().put(carId));
        requestBody.put("endOfRental", endOfRental);
        requestBody.put("tenantId", tenantId);
    }
    //---------------Create new rental
    @Then("the retrieved rental body should contain the {string} {string} and the {string} {string} and the status code {string}")
    public void theRetrievedRentalBodyShouldContainTheAndTheAndTheStatusCode(String field1key, String field1expected, String field2key, String field2expected, String expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
        Assert.assertTrue(responseBody.contains("\""+field1key+"\" : \""+field1expected+"\""));
        Assert.assertTrue(responseBody.contains("\""+field2key+"\" : \""+field2expected+"\""));
    }
    //---------------Check if rented car is available for rental
    @Then("the retrieved body should not contain the {string} {string} and the {string} {string} and the status code {string}")
    public void theRetrievedBodyShouldNotContainTheAndTheAndTheStatusCode(String field1key, String field1expected, String field2key, String field2expected, String expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
        Assert.assertFalse(responseBody.contains("\""+field1key+"\" : \""+field1expected+"\""));
        Assert.assertFalse(responseBody.contains("\""+field2key+"\" : \""+field2expected+"\""));
    }
}