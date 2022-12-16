package com.capgemini.demo.carrental.stepdefs;

import com.capgemini.demo.carrental.config.StepDefsConfig;
import com.capgemini.demo.carrental.model.Car;
import com.capgemini.demo.carrental.model.Rental;
import com.capgemini.demo.carrental.util.ResponseElementsEnum;
import com.capgemini.demo.carrental.util.RestTemplateUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jdk.nashorn.internal.parser.JSONParser;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
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
    private String responseBody;
    private Integer carId;
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
    ResponseEntity<Object> responseEntity;


    @Given("the REST service with initial {string} endpoint is available and the {string} method is supported")
    public void prepareEndpoint(String endpoint, String httpMethod) {
        requestType = HttpMethod.valueOf(httpMethod);
        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint));
    }

    @When("I send a valid request with content type {string} to the service")
    public void sendRequest(String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(contentType));
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestDataModel, headers);
        responseEntity = restTemplate.exchange(requestUrl, requestType, requestEntity, Object.class);
    }

    @Then("The response code is {int}")
    public void theResponseCodeIs(int expectedResponseCode) {
        //Act
        int actualStatusCode = responseEntity.getStatusCodeValue();
        //Assert
        Assert.assertEquals(expectedResponseCode, actualStatusCode);
    }

    @And("The response length is bigger than {int}")
    public void theResponseLengthIs(int expectedResponseLength) throws JSONException {
        //Act
        int actualResponseLength = new JSONArray(responseEntity.getBody()).length();
        //Assert
        Assert.assertTrue(actualResponseLength > expectedResponseLength);
    }

    @And("User prepares a car request body with data")
    public void prepareCareRequestBody(DataTable entry) throws JSONException {
        List<Map<String, String>> requestMaps = entry.asMaps(String.class, String.class);
        requestBody.put("brand", requestMaps.get(0).get("brand"));
        requestBody.put("model", requestMaps.get(0).get("model"));
        requestBody.put("bodyType", requestMaps.get(0).get("bodyType"));
        requestBody.put("fuelType", requestMaps.get(0).get("fuelType"));
        requestBody.put("year", Integer.valueOf(requestMaps.get(0).get("year")));
        requestBody.put("isRented", !requestMaps.get(0).get("isRented").equals("false"));
    }

    @DataTableType
    public Car mapDataTableToCarObject(Map<String, String> entry) {
        Car car = new Car();
        car.setBrand(entry.get("brand"));
        car.setModel(entry.get("model"));
        car.setBodyType(entry.get("bodyType"));
        car.setFuelType(entry.get("fuelType"));
        car.setYear(entry.get("year"));
        car.setIsRented(entry.get("isRented"));
        return car;
    }

    @And("User prepares a car request body with data using dataModel")
    public void userPreparesACarRequestBodyWithDataUsingDataModel(Car carRequestDataModel) {
        requestDataModel = carRequestDataModel;
    }

    @Then("Response should have correct data using dataModel")
    public void responseShouldHaveCorrectDataUsingDataModel() {
        Car  car = (Car) responseEntity.getBody();
        //Assert
        Assert.assertEquals(requestDataModel, car);
    }


//    @And("The car endpoint response has correct data")
//    public void assertResponseHasCorrectData() throws JSONException {
//        JSONObject response = new JSONObject(responseEntity.getBody());
//        response.remove("id");
//        //Assert
//        Assert.assertEquals(requestBody.toString(), response.toString());
//    }

//    @When("the REST service with initial {string} endpoint and created car id is available and the {string} method is supported")
//    public void requestWithId(String endpoint, String httpMethod) throws JSONException {
//        requestType = HttpMethod.valueOf(httpMethod);
//        requestUrl = CAR_SERVICE_ADDRESS.concat(ENDPOINT_SELECTOR.get(endpoint).concat(new JSONObject(responseEntity.getBody()).get("id").toString()));
//    }


    @When("Send GET request to cars endpoint and validate the response")
    public void sendGetRequestToCar() throws JSONException {
        String endpoint = CAR_SERVICE_ADDRESS + ENDPOINT_SELECTOR.get("car");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

        RestTemplate restTemp = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemp.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);

        //Assert
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());


        JSONArray jsonResponse = new JSONArray(responseEntity.getBody());
        int carsAmount = jsonResponse.length();
        Assert.assertEquals(21, carsAmount);
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



}