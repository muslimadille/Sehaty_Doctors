package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName

class LaboratoryPhotos (
    @SerializedName("id")
    var id: Long,
    @SerializedName("featured")
    var featured: String,
    @SerializedName("laboratory_id")
    var laboratory_id: String
)