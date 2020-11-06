package com.muslim_adel.sehatydoctors.remote.objects

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("phonenumber")
    var phone_number: ArrayList<String>,
    @SerializedName("doctor_id")
    var doctor_id:  ArrayList<String>,
    @SerializedName("checkbox")
    var checkbox:ArrayList<String>,
    @SerializedName("email")
    var email: ArrayList<String>,
    @SerializedName("name")
    var name: ArrayList<String>,
    @SerializedName("booking_date")
    var booking_date: ArrayList<String>,
    @SerializedName("gender_id")
    var gender_id: ArrayList<String>,
    @SerializedName("speciality_id")
    var speciality_id: ArrayList<String>
)
