package com.op.astrothings.com.astrothings.presentation.view.login

import com.op.astrothings.com.astrothings.data.model.requestJSON.LoginJson
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPJson

sealed class LoginIntent {
    data class GenerateLoginOTP(val requestOTPJson: RequestOTPJson): LoginIntent()
}