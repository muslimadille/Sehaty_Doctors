package com.muslim_adel.sehatydoctors.modules.registration

data class PharmRegisterationValidator (
    var password:String,
    var phonenumber:String,
    var email:String,
    var gender_id:String,
    var featured:String,
    var firstName_en:String,
    var firstName_ar:String,
    var lastName_en:String,
    var lastName_ar:String,
    var pharmName_ar:String,
    var pharmName_en:String,
    var about_ar:String,
    var about_en:String,
    var practiceLicenseID:String,
    var profissionalTitleID:String,
    var address_en:String,
    var address_ar:String,
    var landmark_en:String,
    var landmark_ar:String,
    var area_id:String,
    var num_of_day:String,
    var lng:String,
    var lat:String,
    var shift:String

)