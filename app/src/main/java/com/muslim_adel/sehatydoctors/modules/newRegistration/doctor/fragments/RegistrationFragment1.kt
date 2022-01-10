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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFragment1 : Fragment() {
    var mContext: DoctorRegistrationScreen?=null



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
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as DoctorRegistrationScreen
    }
    fun handelInputData(){
        mContext!!.doctorRegistrationModel!!.phonenumber= Q.PHONE_KEY+phon_num.text.toString()
    }
}