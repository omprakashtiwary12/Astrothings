package com.op.astrothings.com.astrothings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPJson
import com.op.astrothings.com.astrothings.network.NetworkResponse
import com.op.astrothings.com.astrothings.presentation.view.login.LoginIntent
import com.op.astrothings.com.astrothings.presentation.view.login.LoginState
import com.op.astrothings.com.astrothings.reposeitory.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {
 val loginIntent = Channel<LoginIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            loginIntent.consumeAsFlow().collect {
                when (it) {
                    is LoginIntent.GenerateLoginOTP -> fetchOTP(it.requestOTPJson)
                }
                }
        }
    }
    private fun fetchOTP(requestOTPJson: RequestOTPJson) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val response = repository.requestLoginOTP(requestOTPJson)
                _state.value = when (response) {
                    is NetworkResponse.Success -> {
                        LoginState.RequestOTPSuccess(response.body)
                    }
                    is NetworkResponse.ApiError -> {
                        LoginState.RequestOTPAPIError(response.body)
                    }
                    is NetworkResponse.NetworkError -> {
                        LoginState.RequestOTPNetworkError(response.error.message)
                    }
                    is NetworkResponse.UnknownError -> {
                        LoginState.RequestOTPUnknownError(response.error?.message)
                    }
                }
            }catch (e: HttpException) {
                Timber.tag("API_ERROR").e("Code: " + e.response.code + ", Message: " + e.message + ", Body: " + e.response)
            }
        }
    }
    fun resetState() {
        _state.value = LoginState.Idle
    }
}