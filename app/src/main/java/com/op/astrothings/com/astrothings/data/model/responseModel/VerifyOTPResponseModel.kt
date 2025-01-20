package com.op.astrothings.com.astrothings.data.model.responseModel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VerifyOTPResponseModel(
    @Json(name = "response")
    val data: ResponseData
) {
    @JsonClass(generateAdapter = true)
    data class ResponseData(
        @Json(name = "statusCode")
        var code: Int,
        @Json(name = "message")
        var message: String,
        @Json(name = "payload", ignore = true)
        var data: Payload? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class Payload(
            @Json(name = "token")
            var token: String,
            @Json(name = "userId")
            var userId: String,
            @Json(name = "language")
            var language: String,
            @Json(name = "accountNumber")
            var accountNumber: String,
            @Json(name = "accountType")
            var accountType: String,
            @Json(name = "accountName")
            var accountName: String,
            @Json(name = "deviceOnlineExpiry")
            var deviceOnlineExpiry: String,
        )
    }
}