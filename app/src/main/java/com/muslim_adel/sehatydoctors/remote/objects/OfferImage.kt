package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName

data class OfferImage (
    @SerializedName("id")
    var id:Long,
    @SerializedName("featured")
    var featured:  String
)