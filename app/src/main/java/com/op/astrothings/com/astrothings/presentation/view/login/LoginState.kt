package com.op.astrothings.com.astrothings.presentation.view.login

import com.op.astrothings.com.astrothings.data.model.errorResponseModel.ErrorResponseModel
import com.op.astrothings.com.astrothings.data.model.responseModel.LoginOTPResponseModel
import com.op.astrothings.com.astrothings.data.model.responseModel.LoginResponseModel

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()

    data class RequestOTPSuccess(val loginOtpResponseModel: LoginOTPResponseModel) : LoginState()
    data class RequestOTPAPIError(val errorResponseModel: ErrorResponseModel) : LoginState()
    data class RequestOTPNetworkError(val error: String?) : LoginState()
    data class RequestOTPUnknownError(val error: String?) : LoginState()

}
