package com.op.astrothings.com.astrothings.data.model.requestJSON

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestOTPVerifyJson(
    @Json(name = "mobileNo")
    var phone: String,

    @Json(name = "otp")
    var otp: String
)