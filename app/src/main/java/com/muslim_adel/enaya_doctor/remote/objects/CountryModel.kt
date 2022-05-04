package com.muslim_adel.enaya_doctor.remote.objects
import com.google.gson.annotations.SerializedName

data class CountryModel(
    @SerializedName("id")
    var id: Int? ,
    @SerializedName("name_en")
    var nameEn: String?,
    @SerializedName("name_ar")
    var nameAr: String?,
    @SerializedName("phone_code")
    var phoneCode: String? ,
    @SerializedName("currency_en")
    var currencyEn: String? ,
    @SerializedName("currency_ar")
    var currencyAr: String?

)
