package com.muslim_adel.sehatydoctors.modules.newRegistration.doctor.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.muslim_adel.sehatydoctors.modules.newRegistration.doctor.DoctorRegistrationScreen
import com.muslim_adel.sehatydoctors.modules.registration.VerivicationActivity
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.LoginResponce
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.fragment_registration1.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationFragment1 : Fragment() {
    var mContext: DoctorRegistrationScreen?=null
    var isMobileValid=true
    var isEmailVaild=true
    var isPasswordValid=true
    var vlaidationText=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration_data, container, false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as DoctorRegistrationScreen
    }

    fun validateInputData(){
        mContext!!.doctorRegistrationModel!!.phonenumber= Q.PHONE_KEY+phon_num.text.toString()
        mContext!!.doctorRegistrationModel!!.email= email.text.toString()
        mContext!!.doctorRegistrationModel!!.password= password.text.toString()
        mContext!!.isEmailVerified
    }
    fun checkValidation():Boolean{
        vlaidationText=""
        var value=true
        if(this.phon_num.text.toString().isEmpty()||this.phon_num.text.toString().length!=10){
            value=false
            vlaidationText=vlaidationText+"أدخل رقم هاتف صحيح"+"\n"

        }
        if(this.email.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل بريد إلكتروني صحيح"+"\n"

        }
        if(this.password.text.toString().isEmpty()||this.password.text.toString().length<6){
            value=false
            vlaidationText=vlaidationText+"أدخل رمز دخول يتكون من 6 ارقام او حروف"



        }
        if(!value){
            Toast.makeText(
                mContext,
                vlaidationText,
                Toast.LENGTH_SHORT
            ).show()
        }
        return value
    }


}