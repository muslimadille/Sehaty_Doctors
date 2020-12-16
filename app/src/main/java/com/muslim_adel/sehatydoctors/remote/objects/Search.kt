package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName

data class Search (
    @SerializedName("search")
    var search: List<Doctor>
)

