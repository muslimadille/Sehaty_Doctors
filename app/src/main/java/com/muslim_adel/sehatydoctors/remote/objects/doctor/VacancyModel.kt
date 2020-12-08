package com.muslim_adel.sehatydoctors.remote.objects.doctor

import com.google.gson.annotations.SerializedName

data class VacancyModel (
    @SerializedName("id")
    var id:Int,
    @SerializedName("start_date")
    var start_date: String,
    @SerializedName("end_date")
    var end_date: String,
    @SerializedName("doctor_id")
    var doctor_id:Int,
)