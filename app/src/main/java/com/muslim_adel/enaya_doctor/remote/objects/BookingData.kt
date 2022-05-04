package com.muslim_adel.enaya_doctor.remote.objects

import com.google.gson.annotations.SerializedName

data class BookingData(
    @SerializedName("id")
    var id: Long,
    @SerializedName("created_at")
    var created_at: String,
    @SerializedName("updated_at")
    var updated_at: String,
    @SerializedName("status_id")
    var status_id: Int,
    @SerializedName("doctor_id")
    var doctor_id: String,
    @SerializedName("booking_date")
    var booking_date: String,
    @SerializedName("phonenumber")
    var phonenumber: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("name")
    var name: String
)