package com.schipol.api

import io.restassured.RestAssured.given
import io.restassured.authentication.AuthenticationScheme
import io.restassured.builder.RequestSpecBuilder
import io.restassured.response.Response

class SchipolApiClient(
    private val baseUri: String,
    private val appId: String,
    private val appKey: String
) {

    private fun getFlightsByPage(sortString: String = "", page: Int = 1) =
        if (!sortString.isBlank()) {
            given()
                .spec(RequestSpecBuilder().build())
                .baseUri(baseUri)
                .basePath("/public-flights")
                .queryParam("page", page)
                .queryParam("sort", sortString)
                .header("Accept", "application/json")
                .header("ResourceVersion", "v4")
                .header("app_id", appId)
                .header("app_key", appKey)
                .get("/flights")
                .then()
                .extract()
                .response()
        }
        else {
            given()
                .spec(RequestSpecBuilder().build())
                .baseUri(baseUri)
                .basePath("/public-flights")
                .queryParam("page", page)
                .header("Accept", "application/json")
                .header("ResourceVersion", "v4")
                .header("app_id", appId)
                .header("app_key", appKey)
                .get("/flights")
                .then()
                .extract()
                .response()
        }


        fun getFlights(sortString: String = "", page: Int = 1): Response {
            return getFlightsByPage(sortString, page)
        }

    }
