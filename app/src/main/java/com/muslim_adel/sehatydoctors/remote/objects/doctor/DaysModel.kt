package com.seha_khanah_doctors.remote.objects.doctor
import com.google.gson.annotations.SerializedName

data class DaysModel (
    @SerializedName("id")
    var id:Int,
    @SerializedName("name_en")
    var name_en: String,
    @SerializedName("name_ar")
    var name_ar: String

)