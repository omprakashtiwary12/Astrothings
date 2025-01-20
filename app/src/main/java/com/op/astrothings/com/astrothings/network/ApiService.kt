package com.op.astrothings.com.astrothings.network

import com.op.astrothings.com.astrothings.data.model.errorResponseModel.ErrorResponseModel
import com.op.astrothings.com.astrothings.data.model.requestJSON.LoginJson
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPJson
import com.op.astrothings.com.astrothings.data.model.responseModel.LoginOTPResponseModel
import com.op.astrothings.com.astrothings.data.model.responseModel.LoginResponseModel
import com.op.astrothings.com.astrothings.data.model.responseModel.VerifyOTPResponseModel
import com.op.astrothings.com.astrothings.model.dashboardResponseModel.DashboardResponse
import com.op.astrothings.com.astrothings.network.ApiEndPoint.verifyOtp
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    @POST(ApiEndPoint.loginApi)
    suspend fun requestLoginOTP(
        @Query("mobileNo") mobileNo: String
    ): NetworkResponse<LoginOTPResponseModel, ErrorResponseModel>

    @POST(ApiEndPoint.verifyOtp)
    suspend fun verifyLoginOTP(
        @Query("mobileNo")  mobileNo: String,
        @Query("otp") otp: String
    ): NetworkResponse<VerifyOTPResponseModel, ErrorResponseModel>

    @GET(ApiEndPoint.getAstrologers)
    suspend fun getAstrologers(): NetworkResponse<DashboardResponse, ErrorResponseModel>

}