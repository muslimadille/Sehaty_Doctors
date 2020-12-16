package com.seha_khanah_doctors.remote.objects
import com.google.gson.annotations.SerializedName

data class LaboratoryLoginData(
    @SerializedName("laboratory")
    var user: Laboratory,
    @SerializedName("status")
    var status: Int,
    @SerializedName("token")
    var token: String )

