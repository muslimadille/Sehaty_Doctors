package com.seha_khanah_doctors.remote.objects.doctor

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubSpiecialityModel(
    @SerializedName("id")
    var id: Long,
    @SerializedName("name_en")
    var name_en: String,
    @SerializedName("name_ar")
    var name_ar: String,
    @SerializedName("specialty_id")
    var specialty_id: Int,

) : Parcelable
