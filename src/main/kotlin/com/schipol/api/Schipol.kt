package com.schipol.api

import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.response.Response

class SchipolApiClient(
    private val baseUri: String,
    private val basePath: String,
    private val appId: String,
    private val appKey: String
) {

    private fun apiRequestSpec(
        baseUri: String,
        basePath: String,
        appId: String,
        appKey: String
    ): RequestSpecBuilder {
        return RequestSpecBuilder()
            .setBaseUri(baseUri)
            .setBasePath(basePath)
            .addHeader("ResourceVersion", "v4")
            .addHeader("app_id", appId)
            .addHeader("app_key", appKey)
            .addHeader("Accept", "application/json")
    }

    fun getFlights(sortString: String = "+scheduleTime", page: Int = 0): Response =
        given()
            .spec(apiRequestSpec(baseUri, basePath, appId, appKey).build())
            .queryParam("page", page)
            .queryParam("sort", sortString)
            .get("/flights")
            .then()
            .extract()
            .response()

    fun getFlightsWithConsoleLogs(sortString: String = "+scheduleTime", page: Int = 0): Response =
        given()
            .spec(apiRequestSpec(baseUri, basePath, appId, appKey).build())
            .queryParam("page", page)
            .queryParam("sort", sortString)
            .log()
            .all()
            .get("/flights")
            .then()
            .extract()
            .response()

    fun getDestinations(sortString: String = "+publicName.dutch", page: Int = 0): Response =
        given()
            .spec(apiRequestSpec(baseUri, basePath, appId, appKey).build())
            .queryParam("page", page)
            .queryParam("sort", sortString)
            .get("/destinations")
            .then()
            .extract()
            .response()
}
