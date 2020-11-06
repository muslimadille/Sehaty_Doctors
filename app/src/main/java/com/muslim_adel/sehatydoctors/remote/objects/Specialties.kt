package com.muslim_adel.sehatydoctors.remote.objects

import com.google.gson.annotations.SerializedName

data class Specialties(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name_en")
    var name_en: String,
    @SerializedName("name_ar")
    var name_ar: String,
    @SerializedName("property")
    var property: Int,
    @SerializedName("icon")
    var icon: String

)