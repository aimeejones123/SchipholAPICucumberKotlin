package com.schipol.stepdefs

import com.schipol.hooks.AppConfiguration
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.restassured.response.Response
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = [AppConfiguration::class])
class CommonStepsDefinitions {
    lateinit var response: Response
    lateinit var scenario: Scenario

    @Before
    fun before(scenario: Scenario) {
        this.scenario = scenario
    }
}