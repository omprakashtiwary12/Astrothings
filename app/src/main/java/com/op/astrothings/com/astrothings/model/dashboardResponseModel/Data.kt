package com.op.astrothings.com.astrothings.model.dashboardResponseModel


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data (
    @SerializedName("name"              ) var name              : String?            = null,
    @SerializedName("education"         ) var education         : String?            = null,
    @SerializedName("experienceYears"   ) var experienceYears   : Int?               = null,
    @SerializedName("pricePerMinute"    ) var pricePerMinute    : Int?               = null,
    @SerializedName("location"          ) var location          : String?            = null,
    @SerializedName("bio"               ) var bio               : String?            = null,
    @SerializedName("specializations"   ) var specializations   : ArrayList<String>  = arrayListOf(),
    @SerializedName("languagesKnown"    ) var languagesKnown    : ArrayList<String>  = arrayListOf(),
    @SerializedName("gender"            ) var gender            : String?            = null,
    @SerializedName("profilePictureUrl" ) var profilePictureUrl : String?            = null,
    @SerializedName("availability"      ) var availability      : String?            = null,
    @SerializedName("rating"            ) var rating            : Double?            = null,
    @SerializedName("reviews"           ) var reviews           : ArrayList<Reviews> = arrayListOf()

): Parcelable

