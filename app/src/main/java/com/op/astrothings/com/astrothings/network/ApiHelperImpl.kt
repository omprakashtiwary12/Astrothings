package com.op.astrothings.com.astrothings.network

import com.op.astrothings.com.astrothings.data.model.errorResponseModel.ErrorResponseModel
import com.op.astrothings.com.astrothings.data.model.requestJSON.LoginJson
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPJson
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPVerifyJson
import com.op.astrothings.com.astrothings.data.model.responseModel.LoginOTPResponseModel
import com.op.astrothings.com.astrothings.data.model.responseModel.LoginResponseModel
import com.op.astrothings.com.astrothings.data.model.responseModel.VerifyOTPResponseModel
import com.op.astrothings.com.astrothings.model.dashboardResponseModel.DashboardResponse
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun requestLoginOTP(requestOTPJson: RequestOTPJson): NetworkResponse<LoginOTPResponseModel, ErrorResponseModel> {
        return apiService.requestLoginOTP(mobileNo = requestOTPJson.phone)
    }

    override suspend fun verifyOtp(requestOTPVerifyJson: RequestOTPVerifyJson): NetworkResponse<VerifyOTPResponseModel, ErrorResponseModel> {
        return apiService.verifyLoginOTP(mobileNo = requestOTPVerifyJson.phone, otp = requestOTPVerifyJson.otp)
    }

    override suspend fun getAstrologers(): NetworkResponse<DashboardResponse, ErrorResponseModel> {
        return apiService.getAstrologers()
    }

}