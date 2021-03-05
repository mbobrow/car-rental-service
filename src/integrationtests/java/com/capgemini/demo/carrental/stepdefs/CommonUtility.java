package com.capgemini.demo.carrental.stepdefs;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
public class CommonUtility {

    private String errorResponseStatusCode;
    private String errorResponseBody;

    private RestTemplate restTemplate = new RestTemplate();

    private void interceptErrorResponse(HttpStatusCodeException e) {
        errorResponseStatusCode = e.getStatusCode().toString().replaceAll("\\D+", "");
        errorResponseBody = e.getResponseBodyAsString();
    }

    public ResponseEntity<String> processHttpRequest(HttpMethod httpMethod, String request, String requestUrl, String contentType) {
        HttpEntity<String> entity = createRequestEntity(request, contentType);
        try {
            return restTemplate.exchange(requestUrl, httpMethod, entity, String.class);
        } catch (HttpStatusCodeException e) {
            interceptErrorResponse(e);
        }
        return null;
    }

    private HttpEntity<String> createRequestEntity(String request, String contentType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(contentType));
        HttpEntity<String> entity = new HttpEntity<>(request, httpHeaders);
        return entity;
    }

    public String getErrorResponseStatusCode() {
        return errorResponseStatusCode;
    }

    public String getErrorResponseBody() {
        return errorResponseBody;
    }
}
