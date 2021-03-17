package com.capgemini.demo.carrental.util;

import com.google.common.collect.ImmutableMap;

public class ConstantUtils {

    private ConstantUtils() {
    }

    public static final String LOCAL_HOST = "http://localhost";
    public static final String LOCAL_HOST_PORT = ":8080";
    public static final String API_V1 = "/api/v1/";

    public static final String CAR_SERVICE_ADDRESS = LOCAL_HOST
            .concat(LOCAL_HOST_PORT)
            .concat(API_V1);

    public static final ImmutableMap<String, String> ENDPOINT_SELECTOR = ImmutableMap.<String, String>builder()
            .put("car", "car/")
            .put("cars available", "car?available=true")
            .put("rental", "rental/")
            .put("rental search", "rental/search/")
            .put("rental search by car", "rental/search/byCar/")
            .put("rental search by tenant", "rental/search/byTenant/")
            .put("tenant", "tenant/")
            .put("remove rental", "rental/remove/")
            .build();
}
