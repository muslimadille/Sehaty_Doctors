package com.muslim_adel.sehatydoctors.remote.objects

import com.google.gson.annotations.SerializedName
import com.seha_khanah_doctors.remote.objects.Doctor

data class AllTimeModel (
    @SerializedName("id")
    var id: Int,
    var time_en:String,
    var time_ar:String,
    var duration_en:String,
    var duration_ar:String
)