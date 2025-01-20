package com.op.astrothings.com.astrothings.data.model.requestJSON

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginJson(
    @Json(name = "username") var userName: String,
    @Json(name = "password") var password: String,
    @Json(name = "platform") var platform: String
)