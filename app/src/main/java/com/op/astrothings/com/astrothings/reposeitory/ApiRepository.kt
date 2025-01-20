package com.op.astrothings.com.astrothings.reposeitory

import com.op.astrothings.com.astrothings.data.model.requestJSON.LoginJson
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPJson
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPVerifyJson
import com.op.astrothings.com.astrothings.network.ApiHelper
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiHelper: ApiHelper){
    suspend fun requestLoginOTP(requestOTPJson: RequestOTPJson) = apiHelper.requestLoginOTP(requestOTPJson)
    suspend fun verifyOtp(requestOTPVerifyJson: RequestOTPVerifyJson) = apiHelper.verifyOtp(requestOTPVerifyJson)
    suspend fun getAstrologers() = apiHelper.getAstrologers()
}