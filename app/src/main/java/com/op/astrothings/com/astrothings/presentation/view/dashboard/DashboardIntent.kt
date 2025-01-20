package com.op.astrothings.com.astrothings.presentation.view.dashboard

sealed class DashboardIntent {
    data object GetAstrologers : DashboardIntent()
}