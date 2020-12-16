package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName

data class OfferServicesModel (
    @SerializedName("id")
    var id:Long,
    @SerializedName("name_en")
    var name_en:  String,
    @SerializedName("name_ar")
    var name_ar:String,
    @SerializedName("offer_sub_category_id")
    var offer_sub_category_id: Int,

)