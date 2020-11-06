package com.muslim_adel.sehatydoctors.remote.objects

import com.google.gson.annotations.SerializedName

class Laboratory (
    @SerializedName("id")
    var id: Long,
    @SerializedName("gender_id")
    var gender_id: Int,
    @SerializedName("user_type_id")
    var user_type_id: Int,
    @SerializedName("featured")
    var featured: String,
    @SerializedName("firstName_en")
    var firstName_en: String,
    @SerializedName("firstName_ar")
    var firstName_ar: String,
    @SerializedName("lastName_en")
    var lastName_en: String,
    @SerializedName("lastName_ar")
    var lastName_ar: String,
    @SerializedName("speciality_id")
    var speciality_id: Long,
    @SerializedName("laboratory_name_en")
    var laboratory_name_en: String,
    @SerializedName("laboratory_name_ar")
    var laboratory_name_ar: String,
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
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("lng")
    var lng: Double,
    @SerializedName("area_id")
    var area_id: Int,
    @SerializedName("num_of_day")
    var num_of_day: Int,
    @SerializedName("area")
    var area: Reagons,
    @SerializedName("ratings")
    var ratings: ArrayList<Rates>,
    @SerializedName("laboratory_photos")
    var laboratory_photos: ArrayList<LaboratoryPhotos>,
    @SerializedName("laboratory_services")
    var laboratory_services: ArrayList<LaboratoryServices>,
    @SerializedName("rating")
    var rating: Int,
    @SerializedName("address_en")
    var address_en: String,
    @SerializedName("address_ar")
    var address_ar: String,
    @SerializedName("dates")
    var dates: ArrayList<Date>,
    @SerializedName("visitor_num")
    var visitor_num: Int

)