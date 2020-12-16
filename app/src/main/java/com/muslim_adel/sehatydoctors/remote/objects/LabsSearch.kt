package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName

data class LabsSearch (
    @SerializedName("search")
    var search: List<Laboratory>
)