package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName


data class Dates(
    @SerializedName("dates")
    var dates: ArrayList<Date>
)
data class Date(
    @SerializedName("id")
    var id: Int,
    @SerializedName("date")
    var date: String,
    @SerializedName("day_ar")
    var day_ar: String,
    @SerializedName("day_en")
    var day_en: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("times")
    var times: java.util.ArrayList<Times>
)
data class Times (
    @SerializedName("time")
    var time: String,
    var id:Int,
    @SerializedName("status")
    var status: String


)
