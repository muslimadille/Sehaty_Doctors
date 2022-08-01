package com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.fragment

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.PharmRegisterationActivity
import kotlinx.android.synthetic.main.activity_pharm_registration.*
import kotlinx.android.synthetic.main.fragment_pharm_registeration3.*
import kotlinx.android.synthetic.main.fragment_pharm_registeration3.edit_about_doc_ar_txt
import kotlinx.android.synthetic.main.fragment_pharm_registeration3.edit_about_doc_en_txt
import kotlinx.android.synthetic.main.fragment_pharm_registeration3.pro_shift_check
import java.lang.Error


class PharmRegisterationFragment3 : Fragment() {
    var mContext: PharmRegisterationActivity?=null
    var vlaidationText=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pharm_registeration3, container, false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as PharmRegisterationActivity
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as PharmRegisterationActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handelShiftClick()
    }

    fun validateInputData(){
        mContext!!.pharmRegisterationValidator!!.pharmName_ar=edit_lab_name_ar_txt.text.toString()
        mContext!!.pharmRegisterationValidator!!.pharmName_en=edit_lab_name_en_txt.text.toString()
        mContext!!.pharmRegisterationValidator!!.num_of_day=""
        mContext!!.pharmRegisterationValidator!!.about_ar=edit_about_doc_ar_txt.text.toString()
        mContext!!.pharmRegisterationValidator!!.about_en=edit_about_doc_en_txt.text.toString()

    }
    fun checkValidation():Boolean{
        vlaidationText=""
        var value=true
        if(this.edit_lab_name_ar_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل عن الصيدلية (عربي)"+"\n"

        }
        if(this.edit_about_doc_en_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل عن الصيدلية  english"+"\n"

        }
        if(this.edit_lab_name_ar_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل اسم الصيدلية  english"+"\n"

        }
        if(this.edit_lab_name_en_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل عن الصيدلية  english"+"\n"

        }

        if(mContext!!.pharmRegisterationValidator!!.shift.isEmpty()){
            mContext!!.pharmRegisterationValidator!!.shift="0"
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

    open fun alertNetwork(isExit: Boolean = false) {
        val alertBuilder = AlertDialog.Builder(mContext!!)
        //alertBuilder.setTitle(R.string.error)
        alertBuilder.setMessage(R.string.no_internet)
        if (isExit) {
            alertBuilder.setPositiveButton(R.string.exit) { dialog: DialogInterface, _: Int -> mContext!!.finish() }
        } else {
            alertBuilder.setPositiveButton(R.string.dismiss) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        }
        try {
            if(!mContext!!.isFinishing){
                alertBuilder.show()
            }

        }catch (e: Error){}
    }
    private fun handelShiftClick(){
        pro_shift_check.setOnClickListener {
            if(pro_shift_check.isChecked){
                mContext!!.pharmRegisterationValidator!!.shift="0"
            }else{
                mContext!!.pharmRegisterationValidator!!.shift="1"
            }
        }
    }
}