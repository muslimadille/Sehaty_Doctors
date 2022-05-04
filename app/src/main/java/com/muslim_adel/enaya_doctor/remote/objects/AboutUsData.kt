package com.muslim_adel.enaya_doctor.remote.objects

import com.google.gson.annotations.SerializedName

data class AboutUsData (
    @SerializedName("aboutUs_en")
    var aboutUs_en: String,
    @SerializedName("aboutUs_ar")
    var aboutUs_ar: String
)