package com.muslim_adel.enaya_doctor.remote.objects.doctor

data class DoctorRigistrationValidator (
    var password:String,
    var phonenumber:String,
    var email:String,
    var gender_id:String,
    var featured:String,
    var firstName_en:String,
    var firstName_ar:String,
    var lastName_en:String,
    var lastName_ar:String,
    var specialty_id:String,
    var subSpecialties_id:ArrayList<String>,
    var prefix_title_id:String,
    var profissionalDetails_id:String,
    var profissionalTitle_en:String,
    var profissionalTitle_ar:String,
    var aboutDoctor_ar:String,
    var aboutDoctor_en:String,
    var practiceLicenseID:String,
    var profissionalTitleID:String,
    var price:String,
    var address_en:String,
    var address_ar:String,
    var landmark_en:String,
    var landmark_ar:String,
    var area_id:String,
    var waiting_time:String,
    var num_of_day:String,
    var lng:String,
    var lat:String,
    var countryId:Int





    )