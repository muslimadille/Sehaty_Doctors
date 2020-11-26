package com.muslim_adel.sehatydoctors.remote.objects

import com.google.gson.annotations.SerializedName

data class PharmacyLoginResponce (
    @SerializedName("success")
    var success: Boolean,

    @SerializedName("message")
    var message: Message,

    @SerializedName("data")
    var data: PharmacyLoginData
)