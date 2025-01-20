package com.op.astrothings.com.astrothings.data.model.errorResponseModel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponseModel(
    @Json(name = "code")
    @Transient var code: Int = -1,

    @Json(name = "message")
    var message: String = "",

    @Json(name = "error")
    var error: String = "",

    @Json(name = "status")
    var status: String = "",

    @Json(name = "errors", ignore = true)
    var errors: Errors? = null,

    @Json(name = "status_code")
    var statusCode: Int? = null

) {
    @JsonClass(generateAdapter = true)
    data class Errors(
        @Json(name = "phone_number")
        var phoneNumber: String
    )
}