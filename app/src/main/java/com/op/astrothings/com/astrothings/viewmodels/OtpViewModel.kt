package com.op.astrothings.com.astrothings.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPVerifyJson
import com.op.astrothings.com.astrothings.network.NetworkResponse
import com.op.astrothings.com.astrothings.presentation.view.login.LoginState
import com.op.astrothings.com.astrothings.presentation.view.otp.OTPVerifyState
import com.op.astrothings.com.astrothings.presentation.view.otp.OtpIntent
import com.op.astrothings.com.astrothings.reposeitory.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {
    val otpIntent = Channel<OtpIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<OTPVerifyState>(OTPVerifyState.Idle)
    val state: StateFlow<OTPVerifyState>
    get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            otpIntent.consumeAsFlow().collect {
                when (it) {
                    is OtpIntent.VerifyLoginOTP -> verifyOTP(it.requestOTPVerifyJson)
                }
            }
        }
    }
    private fun verifyOTP(requestOTPJson: RequestOTPVerifyJson) {
        viewModelScope.launch {
            _state.value = OTPVerifyState.Loading
            try {
                val response = repository.verifyOtp(requestOTPJson)
                _state.value = when (response) {
                    is NetworkResponse.Success -> {
                        print("We are in success")
                        OTPVerifyState.VerifyOTPSuccess(response.body)
                    }
                    is NetworkResponse.ApiError -> {
                        OTPVerifyState.VerifyOTPAPIError(response.body)
                    }
                    is NetworkResponse.NetworkError -> {
                        OTPVerifyState.VerifyOTPNetworkError(response.error.message)
                    }
                    is NetworkResponse.UnknownError -> {
                        OTPVerifyState.VerifyOTPUnknownError(response.error?.message)
                    }
                }
            }catch (e: HttpException) {
                Timber.tag("API_ERROR")
                    .e("%s%s", "Code: " + e.message() + ", Message: " + e.message + ", Body: ", e.message())
            }
        }
    }
    fun resetState() {
        _state.value = OTPVerifyState.Idle
    }
}