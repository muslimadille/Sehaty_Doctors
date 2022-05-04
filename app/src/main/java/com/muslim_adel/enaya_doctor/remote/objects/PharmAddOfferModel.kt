package com.muslim_adel.enaya_doctor.remote.objects

import com.google.gson.annotations.SerializedName

data class PharmAddOfferModel (
    @SerializedName("offer")
    var offer: PharmacyOffer,
)