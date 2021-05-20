package com.schipol.stepdefs

import com.google.gson.JsonObject
import com.schipol.api.SchipolApiClient
import com.schipol.helpers.asJsonObject
import io.cucumber.java8.En
import io.restassured.response.Response
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.springframework.beans.factory.annotation.Autowired
import java.lang.Thread.sleep


class SchipolSteps @Autowired constructor(
    private var commonStepsDefinitions: CommonStepsDefinitions,
) : En {
    lateinit var schipolApi: SchipolApiClient

    private var flightsJson = mutableListOf<JsonObject>()
    private var destinationsJson = mutableListOf<JsonObject>()
    private val ausIataCodesToCities: MutableList<Map<Any?, Any?>> = mutableListOf()

    var responseDestinationsList: MutableList<Map<Any?, Any?>> = mutableListOf()


    init {
        Given("I have access to the Schipol API") {
            schipolApi = SchipolApiClient("https://api.schiphol.nl", "/public-flights","bc414cbb", "2a36aa2ed5b29a984d388619cb527046")
        }
        When("all flights leaving Schipol today are retrieved") {
            // get total number of pages in the response headers
            commonStepsDefinitions.response = schipolApi.getFlights()
            assertThat(
                "Response status code was not 200.",
                commonStepsDefinitions.response.statusCode,
                equalTo(200)
            )
            val totalPages = getTotalResponsePages(commonStepsDefinitions.response)
            commonStepsDefinitions.scenario.write("$totalPages Pages total to request data from")

            // Query the API for each page and store flight results
            var currentPage = 0
            while (currentPage < totalPages) {
                commonStepsDefinitions.response = schipolApi.getFlights(page = currentPage)
                assertThat(
                    "Response status code was not 200.",
                    commonStepsDefinitions.response.statusCode,
                    equalTo(200)
                )
                val responseFlightsList = commonStepsDefinitions.response.asJsonObject()["flights"].asJsonArray
                for (flight in responseFlightsList) {
                    val flightObj = flight.asJsonObject
                    flightsJson.add(flightObj)
                }
                // Added a sleep to avoid 429 response code
                sleep(300)
                currentPage++
            }
        }
        Then("verify each flight contains IATA codes") {
            // For each flight, verify the route destinations section has at least one value
            for (flightJsonObj in flightsJson) {
                val iataCodes = flightJsonObj.get("route").asJsonObject.get("destinations").asJsonArray
                assertThat(
                    "IATA Codes not present for ${flightJsonObj.get("flightName")}",
                    iataCodes.size(),
                    greaterThan(0)
                )
                // For each destination value, verify it contains an actual value
                for (code in iataCodes) {
                    assertThat(
                        "IATA Codes is blank for ${flightJsonObj.get("flightName")}",
                        code.toString().isBlank(),
                        equalTo(false)
                    )
                }
            }
        }

        When("all destinations are retrieved in ascending order by country") {
            // get total number of pages in the response headers
            commonStepsDefinitions.response = schipolApi.getDestinations()
            assertThat(
                "Response status code was not 200. Could not get destinations for page 0",
                commonStepsDefinitions.response.statusCode,
                equalTo(200)
            )
            val totalPages = getTotalResponsePages(commonStepsDefinitions.response)
            commonStepsDefinitions.scenario.write("$totalPages Pages total to request data from")

            // Query the API for each page and store destination results
            var currentPage = 0
            while (currentPage < totalPages) {
                commonStepsDefinitions.response =
                    schipolApi.getDestinations(page = currentPage, sortString = "+country")
                assertThat(
                    "Response status code was not 200. Could not get destinations for page $currentPage",
                    commonStepsDefinitions.response.statusCode,
                    equalTo(200)
                )
                responseDestinationsList.addAll(commonStepsDefinitions.response.jsonPath().getList("destinations"))


                for (destination in responseDestinationsList) {
                    if (destination["country"] == "Australia") {
                        ausIataCodesToCities.add(mapOf(Pair("iata", destination["iata"])))
                        ausIataCodesToCities.add(mapOf(Pair("city", destination["city"])))
                    }
                }
                // Added a sleep to avoid 429 response code
                sleep(300)
                currentPage++
            }
        }

        Then("verify a destination exists for Sydney, Australia") {
            val destinations = mutableListOf<Map<Any?, Any?>>()

            // For each flight, verify the route destinations section has at least one value
            for (destination in responseDestinationsList) {
                val city = destination["city"]
                val country = destination["country"]
                destinations.add(mapOf(Pair(city, country)))
            }
            val expPair = Pair("Sydney", "Australia")
            val expMap = mapOf<Any?, Any?>(expPair)
            assertThat(
                "At least one destination found for Sydney, Australia",
                destinations,
                hasItem(expMap)
            )
        }
        And("verify all IATA and destination cities exist for Australia") {
            for (iataCityInfo in ausIataCodesToCities) {
                assertThat(
                    "Missing IATA code for ${iataCityInfo["city"]}",
                    iataCityInfo["iata"],
                    notNullValue()
                )
            }
        }

        Given("I don't have access to the Schipol API") {
            schipolApi = SchipolApiClient("https://api.schiphol.nl", "/public-flights","bc414cbb", "invalidKey")
        }
        When("I try to get flight information") {
            commonStepsDefinitions.response = schipolApi.getFlightsWithConsoleLogs()
        }
        Then("a response code of 403 is returned") {
            assertThat(
                "Expecting 403 status code but got ${commonStepsDefinitions.response.statusCode}",
                commonStepsDefinitions.response.statusCode,
                equalTo(403)
            )
        }
    }
}

fun getTotalResponsePages(response: Response): Int {
    val headerPaginationLinks = response.headers.get("link")
    return if (headerPaginationLinks != null) {
        val lastLink = headerPaginationLinks.toString().split(',').toList().last()
        lastLink.substringBefore('>').substringAfter("page=").toInt()
    } else {
        1
    }
}

