package com.muslim_adel.sehatydoctors.remote.objects

import com.google.gson.annotations.SerializedName

data class Reagons (
    @SerializedName("id")
    var id: Int,
    @SerializedName("area_en")
    var area_en: String,
    @SerializedName("area_ar")
    var area_ar: String

)