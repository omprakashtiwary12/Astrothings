package com.op.astrothings.com.astrothings.network

import com.op.astrothings.com.astrothings.data.model.errorResponseModel.ErrorResponseModel
import com.op.astrothings.com.astrothings.data.model.requestJSON.LoginJson
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPJson
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPVerifyJson
import com.op.astrothings.com.astrothings.data.model.responseModel.LoginOTPResponseModel
import com.op.astrothings.com.astrothings.data.model.responseModel.LoginResponseModel
import com.op.astrothings.com.astrothings.data.model.responseModel.VerifyOTPResponseModel
import com.op.astrothings.com.astrothings.model.dashboardResponseModel.DashboardResponse


interface ApiHelper {
    suspend fun requestLoginOTP(requestOTPJson: RequestOTPJson): NetworkResponse<LoginOTPResponseModel, ErrorResponseModel>
    suspend fun verifyOtp(requestOTPVerifyJson: RequestOTPVerifyJson): NetworkResponse<VerifyOTPResponseModel, ErrorResponseModel>
    suspend fun getAstrologers(): NetworkResponse<DashboardResponse, ErrorResponseModel>
}