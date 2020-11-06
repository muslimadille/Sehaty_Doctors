package com.muslim_adel.sehatydoctors.remote.objects

import com.google.gson.annotations.SerializedName

data class Pharmacy (
    @SerializedName("id")
    var id: Long,
    @SerializedName("user_type_id")
    var user_type_id: Long,
    @SerializedName("featured")
    var featured: String,
    @SerializedName("firstName_en")
    var firstName_en: String,
    @SerializedName("firstName_ar")
    var firstName_ar: String,
    @SerializedName("lastName_ar")
    var lastName_ar: String,
    @SerializedName("lastName_en")
    var lastName_en: String,
    @SerializedName("pharmacy_name_en")
    var pharmacy_name_en: String,
    @SerializedName("pharmacy_name_ar")
    var pharmacy_name_ar: String,
    @SerializedName("about_ar")
    var about_ar: String,
    @SerializedName("about_en")
    var about_en: String,
    @SerializedName("streetName_en")
    var streetName_en: String,
    @SerializedName("streetName_ar")
    var streetName_ar: String,
    @SerializedName("buildingNum_en")
    var buildingNum_en: String,
    @SerializedName("buildingNum_ar")
    var buildingNum_ar: String,
    @SerializedName("role")
    var role: String,
    @SerializedName("apartmentNum_en")
    var apartmentNum_en: String,
    @SerializedName("apartmentNum_ar")
    var apartmentNum_ar: String,
    @SerializedName("landmark_en")
    var landmark_en: String,
    @SerializedName("landmark_ar")
    var landmark_ar: String,
    @SerializedName("area_id")
    var area_id:Int,
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("lng")
    var lng: Double
)