package com.muslim_adel.sehatydoctors.remote.objects

import com.google.gson.annotations.SerializedName

data class PharmAddOfferModel (
    @SerializedName("offer")
    var offer: PharmacyOffer,
)