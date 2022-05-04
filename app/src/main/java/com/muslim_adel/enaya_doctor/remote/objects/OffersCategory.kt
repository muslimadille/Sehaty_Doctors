package com.muslim_adel.enaya_doctor.remote.objects

import com.google.gson.annotations.SerializedName

data class OffersCategory (
    @SerializedName("id")
    var id:Long,
    @SerializedName("name_en")
    var name_en:  String,
    @SerializedName("name_ar")
    var name_ar:String,
    @SerializedName("featured")
    var efeaturedl: String,
    @SerializedName("subcategories")
    var subcategories: ArrayList<OffersSubGategory>
)