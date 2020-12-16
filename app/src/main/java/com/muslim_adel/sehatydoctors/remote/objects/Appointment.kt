package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName

data class Appointment (
    @SerializedName("booking")
    var booking: ArrayList<AppointmentData>
)
