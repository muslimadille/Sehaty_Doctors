package com.muslim_adel.enaya_doctor.remote.objects

import com.google.gson.annotations.SerializedName

data class Search (
    @SerializedName("search")
    var search: List<Doctor>
)

