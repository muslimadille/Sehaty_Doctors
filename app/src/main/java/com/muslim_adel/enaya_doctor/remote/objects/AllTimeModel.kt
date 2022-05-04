package com.muslim_adel.enaya_doctor.remote.objects

import com.google.gson.annotations.SerializedName

data class AllTimeModel (
    @SerializedName("id")
    var id: Int,
    var time_en:String,
    var time_ar:String,
    var duration_en:String,
    var duration_ar:String
)