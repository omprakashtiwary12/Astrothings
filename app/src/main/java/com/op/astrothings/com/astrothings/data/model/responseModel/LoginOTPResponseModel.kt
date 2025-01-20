package com.op.astrothings.com.astrothings.data.model.responseModel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginOTPResponseModel(
    @Json(name = "response")
    val response: Response,
) {
    @JsonClass(generateAdapter = true)
    data class Response(
        @Json(name = "message")
        val message: String,
        @Json(name = "status_code")
        val statusCode: Int
    )
}