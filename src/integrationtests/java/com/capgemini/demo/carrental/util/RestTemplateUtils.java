package com.capgemini.demo.carrental.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class RestTemplateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtils.class);

    private final RestTemplate restTemplate;

    private String errorResponseStatusCode;
    private String errorResponseBody;

    @Autowired
    public RestTemplateUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> processHttpRequest(HttpMethod httpMethod, String requestBody, String requestUrl, String contentType) {
        HttpEntity<String> entity = createRequestEntity(requestBody, contentType);
        try {
            return restTemplate.exchange(requestUrl, httpMethod, entity, String.class);
        } catch (HttpStatusCodeException e) {
            interceptErrorResponse(e);
        }
        return ResponseEntity.badRequest().build();
    }

    private HttpEntity<String> createRequestEntity(String requestBody, String contentType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(contentType));
        return new HttpEntity<>(requestBody, httpHeaders);
    }

    public Map<ResponseElementsEnum, String> retrieveResponseBodyAndStatusCode(ResponseEntity<String> response) {
        Map<ResponseElementsEnum, String> responseElements = new HashMap<>();
        if (!response.getStatusCode().isError()) {
            responseElements.put(ResponseElementsEnum.RESPONSE_STATUS_CODE, String.valueOf(response.getStatusCodeValue()));
            responseElements.put(ResponseElementsEnum.RESPONSE_BODY, response.getBody());
            LOGGER.info("Status code: {}", response.getStatusCodeValue());
        } else {
            responseElements.put(ResponseElementsEnum.RESPONSE_STATUS_CODE, errorResponseStatusCode);
            responseElements.put(ResponseElementsEnum.RESPONSE_BODY, errorResponseBody);
            LOGGER.info("Status code: {}", errorResponseStatusCode);
        }
        return responseElements;
    }

    private void interceptErrorResponse(HttpStatusCodeException e) {
        errorResponseStatusCode = e.getStatusCode().toString().replaceAll("\\D+", "");
        errorResponseBody = e.getResponseBodyAsString();
    }
}
