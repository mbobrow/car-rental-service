package com.capgemini.demo.carrental.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateUtils {

    private final RestTemplate restTemplate;

    private String errorResponseStatusCode;
    private String errorResponseBody;

    @Autowired
    public RestTemplateUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private void interceptErrorResponse(HttpStatusCodeException e) {
        errorResponseStatusCode = e.getStatusCode().toString().replaceAll("\\D+", "");
        errorResponseBody = e.getResponseBodyAsString();
    }

    public ResponseEntity<String> processHttpRequest(HttpMethod httpMethod, String requestBody, String requestUrl, String contentType) {
        HttpEntity<String> entity = createRequestEntity(requestBody, contentType);
        try {
            return restTemplate.exchange(requestUrl, httpMethod, entity, String.class);
        } catch (HttpStatusCodeException e) {
            interceptErrorResponse(e);
        }
        return null;
    }

    private HttpEntity<String> createRequestEntity(String requestBody, String contentType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(contentType));
        return new HttpEntity<>(requestBody, httpHeaders);
    }

    public String getErrorResponseStatusCode() {
        return errorResponseStatusCode;
    }

    public String getErrorResponseBody() {
        return errorResponseBody;
    }
}
