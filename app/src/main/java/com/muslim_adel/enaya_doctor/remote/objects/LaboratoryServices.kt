package com.muslim_adel.enaya_doctor.remote.objects

import com.google.gson.annotations.SerializedName

class LaboratoryServices (
    @SerializedName("id")
    var id: Long,
    @SerializedName("name_en")
    var name_en: String,
    @SerializedName("name_ar")
    var name_ar: String,
    @SerializedName("laboratory_id")
    var laboratory_id: String
)