package com.muslim_adel.enaya_doctor.modules.newRegistration.doctor.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.muslim_adel.enaya_doctor.modules.newRegistration.doctor.DoctorRegistrationScreen
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.fragment_registration3.*
import kotlinx.android.synthetic.main.fragment_registration4.*


class RegistrationFragment4 : Fragment() {

    var mContext: DoctorRegistrationScreen?=null
    private lateinit var swatingTimeSpinnerAdapter: SpinnerAdapterCustomFont
    private var waitingTimeList = ArrayList<String>()
    var vlaidationText=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration4, container, false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as DoctorRegistrationScreen
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as DoctorRegistrationScreen
    }
    fun initSpinner(){
        waitingTimeList.add(getString(R.string.waiting_time))
        waitingTimeList.add("5"+getString(R.string.min))
        waitingTimeList.add("10"+getString(R.string.min))
        waitingTimeList.add("15"+getString(R.string.min))
        waitingTimeList.add("20"+getString(R.string.min))
        waitingTimeList.add("25"+getString(R.string.min))
        waitingTimeList.add("30"+getString(R.string.min))
        waitingTimeList.add("35"+getString(R.string.min))
        waitingTimeList.add("40"+getString(R.string.min))
        waitingTimeList.add("45"+getString(R.string.min))
        waitingTimeList.add("50"+getString(R.string.min))
        waitingTimeList.add("55"+getString(R.string.min))
        waitingTimeList.add("60"+getString(R.string.min))
        swatingTimeSpinnerAdapter = SpinnerAdapterCustomFont(mContext!!, android.R.layout.simple_spinner_item, waitingTimeList)
        swatingTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        swatingTimeSpinnerAdapter.textSize = 12
        re_waiting_time_spinner.adapter = swatingTimeSpinnerAdapter
        swatingTimeSpinnerAdapter.notifyDataSetChanged()
    }
    fun validateInputData(){
        mContext!!.doctorRegistrationModel!!.price=edit_price_txt.text.toString()
        mContext!!.doctorRegistrationModel!!.num_of_day=edit_num_of_days_txt.text.toString()
        mContext!!.doctorRegistrationModel!!.waiting_time="00:30:00"

    }
    fun checkValidation():Boolean{
        vlaidationText=""
        var value=true
        if(this.edit_price_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل سعر الكشف"+"\n"

        }
        if(this.edit_num_of_days_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل عدد أيام العمل"+"\n"

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