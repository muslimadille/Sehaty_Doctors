package com.muslim_adel.sehatydoctors.modules.newRegistration.doctor.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muslim_adel.sehatydoctors.modules.newRegistration.doctor.DoctorRegistrationScreen
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.new_registration_layout.*


class RegistrationFragment2 : Fragment() {
    var mContext: DoctorRegistrationScreen?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration2, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as DoctorRegistrationScreen
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as DoctorRegistrationScreen
    }
    fun handelInputData(){
        mContext!!.doctorRegistrationModel!!.phonenumber= Q.PHONE_KEY+phon_num.text.toString()
    }
}