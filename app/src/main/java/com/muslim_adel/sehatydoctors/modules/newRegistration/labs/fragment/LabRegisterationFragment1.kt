package com.muslim_adel.sehatydoctors.modules.newRegistration.labs.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.muslim_adel.sehatydoctors.modules.newRegistration.labs.LabRegisterationActivity
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.fragment_lab_registeration1.*


class LabRegisterationFragment1 : Fragment() {
    var mContext: LabRegisterationActivity?=null
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lab_registeration1, container, false)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as LabRegisterationActivity
    }
    fun validateInputData(){
        mContext!!.labRegisterationModel!!.phonenumber= Q.PHONE_KEY+phon_num.text.toString()
        mContext!!.labRegisterationModel!!.email= email.text.toString()
        mContext!!.labRegisterationModel!!.password= password.text.toString()
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