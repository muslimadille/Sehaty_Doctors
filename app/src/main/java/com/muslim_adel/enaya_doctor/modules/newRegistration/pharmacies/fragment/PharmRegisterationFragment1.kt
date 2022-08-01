package com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.PharmRegisterationActivity
import com.muslim_adel.enaya_doctor.utiles.Q
import kotlinx.android.synthetic.main.fragment_lab_registeration1.*
import kotlinx.android.synthetic.main.fragment_pharm_registeration1.*
import kotlinx.android.synthetic.main.fragment_pharm_registeration1.conf_password
import kotlinx.android.synthetic.main.fragment_pharm_registeration1.email
import kotlinx.android.synthetic.main.fragment_pharm_registeration1.password
import kotlinx.android.synthetic.main.fragment_pharm_registeration1.phon_num


class PharmRegisterationFragment1 : Fragment() {
    var mContext: PharmRegisterationActivity?=null
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
        return inflater.inflate(R.layout.fragment_pharm_registeration1, container, false)
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as PharmRegisterationActivity
    }
    fun validateInputData(){
        mContext!!.pharmRegisterationValidator!!.phonenumber= Q.PHONE_KEY+phon_num.text.toString()
        mContext!!.pharmRegisterationValidator!!.email= email.text.toString()
        mContext!!.pharmRegisterationValidator!!.password= password.text.toString()
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