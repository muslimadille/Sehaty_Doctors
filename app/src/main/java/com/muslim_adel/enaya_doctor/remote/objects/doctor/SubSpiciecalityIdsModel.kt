package com.muslim_adel.enaya_doctor.remote.objects.doctor

import com.google.gson.annotations.SerializedName

data class SubSpiciecalityIdsModel (
    @SerializedName("id")
    var id:Int,
    @SerializedName("duration_en")
    var duration_en: String,
    @SerializedName("duration_ar")
    var duration_ar: String,
)