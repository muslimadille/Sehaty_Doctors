package com.muslim_adel.enaya_doctor.remote.objects

import com.google.gson.annotations.SerializedName

data class CreateCodeModel (
    @SerializedName("status")
    var status: Long,
    @SerializedName("user")
    var user: User,

    )