package com.op.astrothings.com.astrothings.data.model.responseModel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponseModel(
    @Json(name = "response")
    val data: ResponseData
) {
    @JsonClass(generateAdapter = true)
    data class ResponseData(
        @Json(name = "message")
        val message: String,
        @Json(name = "status_code")
        val code: Int
    )
}