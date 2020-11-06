package com.muslim_adel.sehatydoctors.remote.objects

import com.google.gson.annotations.SerializedName

data class LabsSearch (
    @SerializedName("search")
    var search: List<Laboratory>
)