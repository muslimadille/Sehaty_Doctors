package com.muslim_adel.enaya_doctor.remote.objects

import com.google.gson.annotations.SerializedName

data class OfferSlider (
    @SerializedName("id")
    var id: Long,
    @SerializedName("featured_en")
    var featured_en: String,
    @SerializedName("featured_ar")
    var featured_ar: String
)