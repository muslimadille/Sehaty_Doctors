package com.muslim_adel.sehatydoctors.remote.objects.doctor

import com.google.gson.annotations.SerializedName

data class DurationModel (
    @SerializedName("id")
    var id:Int,
    @SerializedName("duration_en")
    var duration_en: String,
    @SerializedName("duration_ar")
    var duration_ar: String,
)