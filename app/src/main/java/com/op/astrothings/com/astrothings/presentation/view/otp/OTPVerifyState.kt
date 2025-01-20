package com.op.astrothings.com.astrothings.presentation.view.otp

import com.op.astrothings.com.astrothings.data.model.errorResponseModel.ErrorResponseModel
import com.op.astrothings.com.astrothings.data.model.responseModel.VerifyOTPResponseModel

sealed class OTPVerifyState {
    object Idle : OTPVerifyState()
    object Loading : OTPVerifyState()
    data class VerifyOTPSuccess(val otpResponseModel: VerifyOTPResponseModel) : OTPVerifyState()
    data class VerifyOTPAPIError(val errorResponseModel: ErrorResponseModel) : OTPVerifyState()
    data class VerifyOTPNetworkError(val error: String?) : OTPVerifyState()
    data class VerifyOTPUnknownError(val error: String?) : OTPVerifyState()
}