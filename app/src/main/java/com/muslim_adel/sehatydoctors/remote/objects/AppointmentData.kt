package com.muslim_adel.sehatydoctors.remote.objects

import com.google.gson.annotations.SerializedName

class AppointmentData (
    @SerializedName("id")
    var id: Long,
    @SerializedName("client_id")
    var client_id: Long,
    @SerializedName("booking_date")
    var booking_date: String,
    @SerializedName("status_id")
    var status_id: Int,
    @SerializedName("doctor_id")
    var doctor_id: Long,
    @SerializedName("doctor")
    var doctor: Doctor

)