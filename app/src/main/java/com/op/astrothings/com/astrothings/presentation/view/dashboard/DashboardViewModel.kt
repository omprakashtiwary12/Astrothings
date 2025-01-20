package com.op.astrothings.com.astrothings.presentation.view.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.op.astrothings.com.astrothings.network.NetworkResponse
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
class DashboardViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {

    val dashboardIntent = Channel<DashboardIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<DashboardState>(DashboardState.Idle)
    val state: StateFlow<DashboardState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            dashboardIntent.consumeAsFlow().collect {
                when (it) {
                    is DashboardIntent.GetAstrologers -> fetchAstrologers()
                }
            }
        }
    }

    private fun fetchAstrologers() {
       viewModelScope.launch {
           _state.value = DashboardState.Loading
           try {
               val response = repository.getAstrologers()
               print("ResponseFromJson => $response")
               _state.value = when (response) {
                   is NetworkResponse.Success -> {
                       DashboardState.DashboardSuccess(response.body)
                   }
                   is NetworkResponse.ApiError -> DashboardState.DashboardAPIError(response.body)
                   is NetworkResponse.NetworkError -> DashboardState.DashboardNetworkError(response.error.message)
                   is NetworkResponse.UnknownError -> DashboardState.DashboardUnknownError(response.error?.message)
               }

           }catch (e: HttpException) {
               Timber.tag("API_ERROR").e(" Message: " + e.message)
           }
       }
    }
}