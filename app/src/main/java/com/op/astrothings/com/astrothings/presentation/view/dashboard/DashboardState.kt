package com.op.astrothings.com.astrothings.presentation.view.dashboard

import com.op.astrothings.com.astrothings.data.model.errorResponseModel.ErrorResponseModel
import com.op.astrothings.com.astrothings.model.dashboardResponseModel.DashboardResponse

sealed class DashboardState {
    object Idle : DashboardState()
    object Loading : DashboardState()

    data class DashboardSuccess(val dashboardResponseModel: DashboardResponse) : DashboardState()
    data class DashboardAPIError(val errorResponseModel: ErrorResponseModel) : DashboardState()
    data class DashboardNetworkError(val error: String?) : DashboardState()
    data class DashboardUnknownError(val error: String?) : DashboardState()
}