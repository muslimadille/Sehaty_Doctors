package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName

data class PharmAddOfferModel (
    @SerializedName("offer")
    var offer: PharmacyOffer,
)