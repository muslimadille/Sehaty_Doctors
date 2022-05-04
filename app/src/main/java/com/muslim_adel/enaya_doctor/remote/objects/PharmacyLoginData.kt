package com.muslim_adel.enaya_doctor.remote.objects

import com.google.gson.annotations.SerializedName

data class PharmacyLoginData(
    @SerializedName("pharmacy")
    var user: Pharmacy,
    @SerializedName("status")
    var status: Int,
    @SerializedName("token")
    var token: String )

