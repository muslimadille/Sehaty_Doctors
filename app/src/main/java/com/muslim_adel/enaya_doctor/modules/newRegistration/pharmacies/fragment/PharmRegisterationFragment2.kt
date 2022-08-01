package com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.PharmRegisterationActivity
import kotlinx.android.synthetic.main.fragment_pharm_registeration2.*
import java.io.File
import java.util.*


class PharmRegisterationFragment2 : Fragment() {
    var mContext: PharmRegisterationActivity?=null
    var gender=-1
    var selectedImage: File? = null
    var selectedpracticeLicenseIDImage: File? = null
    var selectedprofissionalTitleIDImage: File? = null
    var is_practiceLicenseID_clecked=false
    var is_profissionalTitleID_clecked=false
    var is_profile_img_clecked=false
    var vlaidationText=""
   
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pharm_registeration2, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handelRdioStates()
        onpracticeLicenseIDImageClicked()
        onSelectIMageClicked()
        onprofissionalTitleIDImageClicked()

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as PharmRegisterationActivity
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as PharmRegisterationActivity
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun validateInputData(){
        mContext!!.pharmRegisterationValidator!!.firstName_ar=editee_fn_ar_txt.text.toString().split(" ")[0].toString()
        mContext!!.pharmRegisterationValidator!!.lastName_ar=editee_fn_ar_txt.text.toString().split(" ")[1].toString()
        mContext!!.pharmRegisterationValidator!!.firstName_en=editee_fn_en_txt.text.toString().split(" ")[0].toString()
        mContext!!.pharmRegisterationValidator!!.lastName_en=editee_fn_en_txt.text.toString().split(" ")[1].toString()
        handelImages()
        mContext!!.pharmRegisterationValidator!!.gender_id=gender.toString()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handelImages(){
        var img=""
        if (selectedImage != null) {
            img = "data:image/${selectedImage!!.extension};base64,"+toBase64(selectedImage.toString())
        }
        mContext!!.pharmRegisterationValidator!!.featured=img
        var img2=""
        if (selectedpracticeLicenseIDImage != null) {
            img2 = "data:image/${selectedpracticeLicenseIDImage!!.extension};base64,"+toBase64(selectedpracticeLicenseIDImage.toString())
        }
        mContext!!.pharmRegisterationValidator!!.practiceLicenseID=img2
        var img3=""
        if (selectedprofissionalTitleIDImage != null) {
            img3 = "data:image/${selectedprofissionalTitleIDImage!!.extension};base64,"+toBase64(selectedprofissionalTitleIDImage.toString())
        }
        mContext!!.pharmRegisterationValidator!!.profissionalTitleID=img3
    }
    private fun onSelectIMageClicked(){
        edit_doc_profile_img.setOnClickListener {
            is_profile_img_clecked=true
            is_practiceLicenseID_clecked=false
            is_profissionalTitleID_clecked=false
            selectProfileImage()

        }
    }
    private fun onpracticeLicenseIDImageClicked(){
        edit_practiceLicenseIDImage_img.setOnClickListener {
            is_profile_img_clecked=false
            is_practiceLicenseID_clecked=true
            is_profissionalTitleID_clecked=false
            selectProfileImage()

        }
    }
    private fun onprofissionalTitleIDImageClicked(){
        edit_profissionalTitleID_img.setOnClickListener {
            is_profile_img_clecked=false
            is_practiceLicenseID_clecked=false
            is_profissionalTitleID_clecked=true
            selectProfileImage()

        }
    }
    fun selectProfileImage() {
        TedPermission.with(mContext!!)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    ImagePicker.with(mContext!!)
                        .compress(1024)         //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent { intent ->
                            mContext!!.startForProfileImageResult.launch(intent)
                        }
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(mContext!!,
                        "getString(R.string.permissionDenied)", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .setPermissions(Manifest.permission.CAMERA)
            .check()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64(filePath: String): String{
        val bytes = File(filePath).readBytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)
        return base64
    }

    private fun handelRdioStates(){
        gender_male.isChecked=true
        gender=1
        gender_male.setOnClickListener {
            gender_male.isChecked=true
            gender_female.isChecked=false
            gender=1
            if(gender_male.isChecked){
                gender=1
                gender_female.isChecked=false
            }else if(gender_female.isChecked){
                gender=2
                gender_male.isChecked=false
            }
        }
        gender_female.setOnClickListener {
            gender_female.isChecked=true
            gender_male.isChecked=false
            gender=2
            if(gender_male.isChecked){
                gender=1
                gender_female.isChecked=false
            }else if(gender_female.isChecked){
                gender=2
                gender_male.isChecked=false
            }
        }

    }
    fun checkValidation():Boolean{
        vlaidationText=""
        var value=true
        if(this.editee_fn_ar_txt.text.toString().isEmpty()||!this.editee_fn_ar_txt.text.contains(" ")){
            value=false
            vlaidationText=vlaidationText+"أدخل الاسم واللقب عربي"+"\n"

        }
        if(this.editee_fn_en_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل الاسم واللقب english"+"\n"

        }
        if(selectedImage==null){
            value=false
            vlaidationText=vlaidationText+"أختر الصورة الشخصية"+"\n"
        }
        if(selectedpracticeLicenseIDImage==null){
            value=false
            vlaidationText=vlaidationText+"أختر صورة شهادة المزاولة"+"\n"
        }
        if(selectedprofissionalTitleIDImage==null){
            value=false
            vlaidationText=vlaidationText+"أختر صورة الرخصة"+"\n"
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