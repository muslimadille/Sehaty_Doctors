package com.muslim_adel.sehatydoctors.remote.objects.doctor

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
    var date: String,
    var time: String
)
