package com.muslim_adel.enaya_doctor.modules.newRegistration.labs.fragment

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
import com.muslim_adel.enaya_doctor.modules.newRegistration.labs.LabRegisterationActivity
import kotlinx.android.synthetic.main.fragment_lab_registeration3.*
import java.lang.Error


class LabRegisterationFragment3 : Fragment() {
    var mContext: LabRegisterationActivity?=null
    var vlaidationText=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lab_registeration3, container, false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as LabRegisterationActivity
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as LabRegisterationActivity
    }

    fun validateInputData(){
        mContext!!.labRegisterationModel!!.laboratory_name_ar=edit_lab_name_ar_txt.text.toString()
        mContext!!.labRegisterationModel!!.laboratory_name_en=edit_lab_name_en_txt.text.toString()
        mContext!!.labRegisterationModel!!.num_of_day=edit_num_of_days_txt.text.toString()
        mContext!!.labRegisterationModel!!.about_ar=edit_about_doc_ar_txt.text.toString()
        mContext!!.labRegisterationModel!!.about_en=edit_about_doc_en_txt.text.toString()

    }
    fun checkValidation():Boolean{
        vlaidationText=""
        var value=true
        if(this.edit_lab_name_ar_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل عن المختبر (عربي)"+"\n"

        }
        if(this.edit_about_doc_en_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل عن المختبر  english"+"\n"

        }
        if(this.edit_lab_name_ar_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل اسم المختبر  english"+"\n"

        }
        if(this.edit_lab_name_en_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل عن المختبر  english"+"\n"

        }

        if(this.edit_num_of_days_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل  عدد ايام العمل"+"\n"

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

}