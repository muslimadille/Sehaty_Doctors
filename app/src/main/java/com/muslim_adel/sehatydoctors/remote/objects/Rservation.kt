package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName

data class Rservation (
    @SerializedName("booking")
    var booking: AppointmentData
)
