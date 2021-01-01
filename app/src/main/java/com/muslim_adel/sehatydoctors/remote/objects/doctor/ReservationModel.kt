package com.seha_khanah_doctors.remote.objects.doctor

import com.google.gson.annotations.SerializedName

data class ReservationModel (
    @SerializedName("id")
    var id:Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("phonenumber")
    var phonenumber:String,
    @SerializedName("client_id")
    var client_id: Int,
    @SerializedName("doctor_id")
    var doctor_id: Int,
    @SerializedName("booking_date")
    var booking_date: String,
    @SerializedName("status_id")
    var status_id: Int,
    var date: String,
    var time: String
)
