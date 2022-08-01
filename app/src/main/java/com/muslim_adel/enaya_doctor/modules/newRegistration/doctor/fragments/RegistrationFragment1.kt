package com.muslim_adel.enaya_doctor.modules.newRegistration.doctor.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.muslim_adel.enaya_doctor.modules.newRegistration.doctor.DoctorRegistrationScreen
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.utiles.Q
import kotlinx.android.synthetic.main.fragment_registration1.*

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

        if(this.password.text.toString().isEmpty()||this.password.text.toString().length<6){
            value=false
            vlaidationText=vlaidationText+"أدخل رمز دخول يتكون من 6 ارقام او حروف"

        }
        if(this.conf_password.text.toString().isEmpty()|| this.password.text.toString() != this.conf_password.text.toString()){
            value=false
            vlaidationText=vlaidationText+"رمز الدخول غير متطابق"
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