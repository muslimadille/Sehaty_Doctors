package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("doctor")
    var user: Doctor,
    @SerializedName("status")
    var status: Int,
    @SerializedName("token")
    var token: String )

