package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName

data class Offer (
    @SerializedName("id")
    var id:Long,
    @SerializedName("offer_category_id")
    var offer_category_id:  Int,
    @SerializedName("offer_sub_category_id")
    var offer_sub_category_id:Int,
    @SerializedName("offer_service_id")
    var offer_service_id: Int,
    @SerializedName("offer_sub_service_id")
    var offer_sub_service_id:Int,
    @SerializedName("offer_unit_id")
    var offer_unit_id:Int,
    @SerializedName("unit_number")
    var unit_number:  Int,
    @SerializedName("device_name_en")
    var device_name_en:String,
    @SerializedName("device_name_ar")
    var device_name_ar: String,
    @SerializedName("title_en")
    var title_en:String,
    @SerializedName("title_ar")
    var title_ar:String,
    @SerializedName("description_en")
    var description_en:  String,
    @SerializedName("description_ar")
    var description_ar:String,
    @SerializedName("price")
    var price: Long,
    @SerializedName("discount")
    var discount:Int,
    @SerializedName("date_from")
    var date_from:String,
    @SerializedName("date_to")
    var date_to:  String,
   /* @SerializedName("booking_num")
    var booking_num:Int,*/
    @SerializedName("doctor_id")
    var doctor_id: Long,
    @SerializedName("active")
    var active: Int,
    /*@SerializedName("doctor")
    var doctor:Doctor,
    @SerializedName("rating")
    var rating:Int,*/
    @SerializedName("images")
    var images:  List<OfferImage>,
    /*@SerializedName("dates")
    var dates:  List<Date>,
    @SerializedName("ratings")
    var ratings:  List<Rates>*/



)