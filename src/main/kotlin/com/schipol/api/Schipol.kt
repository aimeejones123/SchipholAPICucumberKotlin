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

//    fun apiRequestSpec(
//        baseUri: String,
//        basePath: String,
//        authScheme: AuthenticationScheme
//    ): RequestSpecBuilder {
//        return RequestSpecBuilder()
//            .setAuth(authScheme)
//            .setBaseUri(baseUri)
//            .setBasePath(basePath)
//    }

    private fun getFlightsByPage(sortString: String = "", page: Int = 1) =
        if (sortString.isNotBlank()) {
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
        } else {
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

    private fun getDestinationsByPage(sortString: String = "", page: Int = 1) =
        if (sortString.isNotBlank()) {
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
                .get("/destinations")
                .then()
                .extract()
                .response()
        } else {
            given()
                .spec(RequestSpecBuilder().build())
                .baseUri(baseUri)
                .basePath("/public-flights")
                .queryParam("page", page)
                .header("Accept", "application/json")
                .header("ResourceVersion", "v4")
                .header("app_id", appId)
                .header("app_key", appKey)
                .get("/destinations")
                .then()
                .extract()
                .response()
        }

    fun getFlights(sortString: String = "", page: Int = 1): Response {
        return getFlightsByPage(sortString, page)
    }

    fun getDestinations(sortString: String = "", page: Int = 1): Response {
        return getDestinationsByPage(sortString, page)
    }

}
