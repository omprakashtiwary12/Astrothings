package com.op.astrothings.com.astrothings.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.op.astrothings.com.astrothings.data.model.responseModel.LoginOTPResponseModel
import java.lang.reflect.Type

class NetworkResponseAdapter : JsonDeserializer<NetworkResponse<Any, Any>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): NetworkResponse<Any, Any> {
        val jsonObject = json.asJsonObject

        return if (jsonObject.has("message") && jsonObject.has("status_code")) {
            val successBody = context.deserialize<LoginOTPResponseModel>(json, LoginOTPResponseModel::class.java)
            NetworkResponse.Success(successBody)
        } else if (jsonObject.has("error")) {
            val errorBody = jsonObject.get("error").asString
            val code = jsonObject.get("status").asInt
            NetworkResponse.ApiError(errorBody, code)
        } else {
            NetworkResponse.UnknownError(null)
        }
    }
}
