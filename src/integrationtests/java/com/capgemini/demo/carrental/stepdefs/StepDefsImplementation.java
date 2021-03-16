package com.capgemini.demo.carrental.stepdefs;

import static com.capgemini.demo.carrental.util.ConstantUtils.API_V1;
import static com.capgemini.demo.carrental.util.ConstantUtils.LOCAL_HOST;
import static com.capgemini.demo.carrental.util.ConstantUtils.LOCAL_HOST_PORT;

import com.capgemini.demo.carrental.util.ResponseElementsEnum;
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

import java.util.Map;

@CucumberContextConfiguration
@SpringBootTest
@ContextConfiguration(classes = {StepDefsConfig.class})
public class StepDefsImplementation {

    private static final Logger logger = LoggerFactory.getLogger(StepDefsImplementation.class);
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
    private Integer id;

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
        requestUrl = CAR_SERVICE_ADDRESS.concat(immutableMap.get(endpoint));
        requestAsString = "";
    }

    @Then("the retrieved body should contains the list of cars and the status code {string}")
    public void theRetrievedBodyShouldContainsTheListOfCarsAndTheStatusCode(String expectedStatusCode) throws JSONException {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
        JSONArray jsonResponseBody = new JSONArray(responseBody);
        Assert.assertTrue(jsonResponseBody.length()==21);
    }
    //---------------Add car and remove it
    @Given("the REST service with {string} brand {string}, model {string}, body type {string}, fuel type {string} and year of production {int} is available and the {string} method is supported")
    public void theRESTServiceWithCarBrandModelBodyTypeFuelTypeAndYearOfProductionIsAvailableAndTheMethodIsSupported(String endpoint, String brand, String model, String type, String fuel, int year, String httpMethod) {
    requestType = HttpMethod.valueOf(httpMethod);
    requestUrl = CAR_SERVICE_ADDRESS.concat(immutableMap.get(endpoint));
    requestAsString = "{\"bodyType\": \""+type+"\",\"brand\": \""+brand+"\",\"fuelType\": \""+fuel+"\",\"model\": \""+model+"\",\"year\": "+year+"}";
    }

    @Then("the retrieved body should contain the {string} of the {string} {string} and the status code {string}")
    public void theRetrievedBodyShouldContainTheOfTheAddedCarAndTheStatusCode(String fieldKey, String operationType, String endpoint, String expectedStatusCode) throws JSONException {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
        JSONObject jsonResponseBody = new JSONObject(responseBody);
        id = (Integer) jsonResponseBody.get(fieldKey);
        Assert.assertNotNull(id);
    }

    @Given("the REST service with previously created {string} id is available and the {string} method is supported")
    public void theRESTServiceWithPreviouslyCreatedIdIsAvailableAndTheMethodIsSupported(String endpoint, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(immutableMap.get(endpoint)).concat(Integer.toString(id));
        requestAsString = "";
    }
    //---------------Remove the car that does not exist
    @Then("the response status code {string}")
    public void theResponseStatusCode(String expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
    }
    //---------------Change year of the car
    @Given("the REST service with {string} id {string}, brand {string}, model {string}, body type {string}, fuel type {string} and year of production {string} is available and the {string} method is supported")
    public void theRESTServiceWithCarIdCarBrandModelBodyTypeFuelTypeAndYearOfProductionIsAvailableAndTheMethodIsSupported(String endpoint, String id, String brand, String model, String type, String fuel, String year, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(immutableMap.get(endpoint)).concat(id);
        requestAsString = "{\"bodyType\": \""+type+"\",\"brand\": \""+brand+"\",\"fuelType\": \""+fuel+"\",\"model\": \""+model+"\",\"year\": "+year+"}";
    }
    //---------------Add a car to non-existing rental
    @Given("the REST service for {string} with beginning date {string}, end date {string}, car id {string} and tenant id {string} is available and the {string} method is supported")
    public void theRESTServiceForWithBeginningDateEndDateCarIdAndTenantIdIsAvailableAndTheMethodIsSupported(String endpoint, String beginningOfRental, String endOfRental, String carId, String tenantId, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(immutableMap.get(endpoint));
        requestAsString = "{ \"beginningOfRental\": \""+beginningOfRental+"\", \"carIds\": [ "+carId+" ], \"endOfRental\": \""+endOfRental+"\", \"tenantId\": "+tenantId+"}";
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