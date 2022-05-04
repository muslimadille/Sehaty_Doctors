package com.muslim_adel.enaya_doctor.remote.objects.doctor

import com.google.gson.annotations.SerializedName
import com.muslim_adel.enaya_doctor.remote.objects.Times

data class WorkingDatesModel (
    @SerializedName("id")
    var id:Int,
    @SerializedName("day_id")
    var day_id: Int,
    @SerializedName("time_from_id")
    var time_from_id: Int,
    @SerializedName("time_to_id")
    var time_to_id:Int,
    @SerializedName("duration_id")
    var duration_id: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("doctor_id")
    var doctor_id:Int,
    @SerializedName("day")
    var day: DaysModel,
    @SerializedName("time_from")
    var time_from: workingTimeModel,
    @SerializedName("time_to")
    var time_to:workingTimeModel,
    @SerializedName("duration")
    var duration: DurationModel,

)