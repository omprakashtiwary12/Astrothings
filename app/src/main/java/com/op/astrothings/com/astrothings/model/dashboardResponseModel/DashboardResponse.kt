package com.op.astrothings.com.astrothings.model.dashboardResponseModel

import com.google.gson.annotations.SerializedName

data class DashboardResponse (
    @SerializedName("statusCode" ) var statusCode : Int?            = null,
    @SerializedName("message"    ) var message    : String?         = null,
    @SerializedName("data"       ) var data       : ArrayList<Data> = arrayListOf()

)
