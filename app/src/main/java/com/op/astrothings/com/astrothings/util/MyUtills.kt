package com.op.astrothings.com.astrothings.util

import com.google.gson.Gson
import com.op.astrothings.com.astrothings.model.dashboardResponseModel.Data

fun Data.toJson(): String {
    return Gson().toJson(this)
}

fun String.toData(): Data {
    return Gson().fromJson(this, Data::class.java)
}