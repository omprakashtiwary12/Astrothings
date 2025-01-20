package com.op.astrothings.com.astrothings.presentation.view.otp

import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPVerifyJson

sealed class OtpIntent {
    data class VerifyLoginOTP(val requestOTPVerifyJson: RequestOTPVerifyJson): OtpIntent()
}