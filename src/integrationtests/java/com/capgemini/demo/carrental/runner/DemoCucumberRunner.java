package com.capgemini.demo.carrental.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:features/DemoFeature.feature"},
        glue = {"com.capgemini.demo.carrental.stepdefs"},
        plugin = {"pretty", "html:target/DemoFeatureReport.html"}
)
public class DemoCucumberRunner {
}
