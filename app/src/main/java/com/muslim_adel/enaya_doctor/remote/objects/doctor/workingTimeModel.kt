package com.muslim_adel.enaya_doctor.remote.objects.doctor

import com.google.gson.annotations.SerializedName

data class workingTimeModel (
    @SerializedName("id")
    var id:Int,
    @SerializedName("time_en")
    var time_en: String,
    @SerializedName("time_ar")
    var time_ar: String,
)