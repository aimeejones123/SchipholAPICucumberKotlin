package com.schiphol.helpers

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.restassured.response.Response

fun Response.asJsonObject(): JsonObject {
    return JsonParser.parseString(this.asString()).asJsonObject
}
