package com.muslim_adel.sehatydoctors.remote.objects

import com.google.gson.annotations.SerializedName

data class PharmacyOffer (
    @SerializedName("id")
    var id: Long,
    @SerializedName("title_en")
    var title_en: String,
    @SerializedName("title_ar")
    var title_ar: String,
    @SerializedName("price")
    var price: Long,
    @SerializedName("featured")
    var featured: String,
    @SerializedName("pharmacy_id")
    var pharmacy_id: String,
    @SerializedName("pharmacy")
    var pharmacy: Pharmacy

)