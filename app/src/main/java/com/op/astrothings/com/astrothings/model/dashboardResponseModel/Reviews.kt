package com.op.astrothings.com.astrothings.model.dashboardResponseModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reviews (

    @SerializedName("reviewer"   ) var reviewer   : String? = null,
    @SerializedName("reviewDate" ) var reviewDate : String? = null,
    @SerializedName("comments"   ) var comments   : String? = null,
    @SerializedName("rating"     ) var rating     : Double? = null

): Parcelable
