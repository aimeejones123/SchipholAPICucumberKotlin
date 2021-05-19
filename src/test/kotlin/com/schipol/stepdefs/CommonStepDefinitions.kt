package com.schipol.stepdefs

import com.schipol.hooks.AppConfiguration
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.java.en.Then
import io.restassured.response.Response
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = [AppConfiguration::class])
class CommonStepsDefinitions {
    lateinit var response: Response
    lateinit var scenario: Scenario
    lateinit var expErrorMsg: String

    @Before
    fun before(scenario: Scenario) {
        this.scenario = scenario
    }
}