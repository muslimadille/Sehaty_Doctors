package com.seha_khanah_doctors.modules.profile.doctor

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.muslim_adel.sehatydoctors.modules.profile.edit_password.EditPasswordActivity
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.modules.base.GlideObject
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.modules.map.MapsActivity
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.*
import com.seha_khanah_doctors.remote.objects.doctor.DaysModel
import com.seha_khanah_doctors.remote.objects.doctor.DoctorProfileModel
import com.seha_khanah_doctors.remote.objects.doctor.SubSpiecialityModel
import com.seha_khanah_doctors.utiles.Q
import com.seha_khanah_doctors.utiles.SpinnerAdapterCustomFont
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_doctor_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class DoctorEditProfileActivity : BaseActivity() {
    private var selectedImage: File? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    var doctorProfileModel: DoctorProfileModel? =null



    private lateinit var swatingTimeSpinnerAdapter: SpinnerAdapterCustomFont
    private var waitingTimeList = ArrayList<String>()
    private lateinit var profDetailsSpinnerAdapter: SpinnerAdapterCustomFont
    private var profDetailsList = ArrayList<String>()

    private var subSpicList = ArrayList<SubSpiecialityModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_edit_profile)
        onSelectIMageClicked()
        fillSpinners()
        profDetailsObserver()
        SubSpiecObserver()
        ObserveDoctorProfile()
        onSaveClicked()
        onEditAddressClicked()
        onEditPasswordClicked()
    }
    private fun profDetailsObserver() {
        if(true){
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).doctorProfissionalDetails()
                .enqueue(object : Callback<BaseResponce<List<DaysModel>>> {
                    override fun onFailure(
                        call: Call<BaseResponce<List<DaysModel>>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<List<DaysModel>>>,
                        response: Response<BaseResponce<List<DaysModel>>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                response.body()!!.data.let {
                                    it!!.forEach { prof: DaysModel->
                                        if (preferences!!.getString("language","")=="Arabic"){
                                            profDetailsList.add(prof.name_ar)

                                        }else{
                                            profDetailsList.add(prof.name_en)

                                        }
                                    }
                                    profDetailsSpinnerAdapter.notifyDataSetChanged()

                                }

                            } else {
                                Toast.makeText(
                                    this@DoctorEditProfileActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            Toast.makeText(this@DoctorEditProfileActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    private fun SubSpiecObserver() {
        if(true){
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).doctorSubSpic()
                .enqueue(object : Callback<BaseResponce<List<SubSpiecialityModel>>> {
                    override fun onFailure(
                        call: Call<BaseResponce<List<SubSpiecialityModel>>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<List<SubSpiecialityModel>>>,
                        response: Response<BaseResponce<List<SubSpiecialityModel>>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                response.body()!!.data.let {
                                    subSpicList.addAll(it!!)
                                    it!!.forEach { prof: SubSpiecialityModel->
                                        if (preferences!!.getString("language","")=="Arabic"){

                                        }else{

                                        }
                                    }

                                }

                            } else {
                                Toast.makeText(
                                    this@DoctorEditProfileActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            Toast.makeText(this@DoctorEditProfileActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }

    private fun onSelectIMageClicked(){
        edit_doc_profile_img.setOnClickListener {
            selectImage()
        }
    }
    fun selectImage() {
        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) CropImageView.CropShape.RECTANGLE else CropImageView.CropShape.OVAL)
                        .setAllowFlipping(false)
                        .setAllowRotation(false)
                        .setCropMenuCropButtonIcon(R.drawable.ic_add)
                        .setAspectRatio(1, 1)
                        .start(this@DoctorEditProfileActivity)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(this@DoctorEditProfileActivity,
                        "getString(R.string.permissionDenied)", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .setPermissions(Manifest.permission.CAMERA)
            .check()
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result: CropImage.ActivityResult? = null
            data?.let { result = CropImage.getActivityResult(data) }
            if (resultCode == RESULT_OK) {
                result?.let {
                    selectedImage = File(result!!.uri!!.path!!)

                    GlideObject.GlideProfilePic(this, selectedImage!!.path, edit_doc_profile_img)
//                    Picasso.get().load(selectedImage!!).fit().centerCrop().into(ivUserImage )
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result!!.error!!.printStackTrace()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64(filePath: String): String{
        val bytes = File(filePath).readBytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)
        return base64
    }

    private  fun fillSpinners(){
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
        swatingTimeSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, waitingTimeList)
        swatingTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        swatingTimeSpinnerAdapter.textSize = 12
        waiting_time_spinner.adapter = swatingTimeSpinnerAdapter
        swatingTimeSpinnerAdapter.notifyDataSetChanged()


        profDetailsSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, profDetailsList)
        profDetailsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        profDetailsSpinnerAdapter.textSize = 12
        prof_details__spinner.adapter = profDetailsSpinnerAdapter

    }
    private  fun setProfileData(profileModel:DoctorProfileModel){
        intent=Intent(this,MainActivity::class.java)
        intent.putExtra("key",1)
        startActivity(intent)
    }
    private fun ObserveDoctorProfile(){
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).fitchDoctorProfile()
            .enqueue(object : Callback<BaseResponce<DoctorProfileModel>> {
                override fun onFailure(call: Call<BaseResponce<DoctorProfileModel>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<DoctorProfileModel>>,
                    response: Response<BaseResponce<DoctorProfileModel>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                doctorProfileModel=it
                                fillProfileData()
                            }
                        } else {
                        }

                    } else {
                    }

                }


            })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onSaveClicked(){
        doc_updat_btn.setOnClickListener {
            upDateDoctorProfile()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateDoctorProfile(){
        onObserveStart()
        apiClient = ApiClient()
        var ssList = ArrayList<String>()
        ssList.add("1")
        var img=""
        if (selectedImage != null) {
            img = "data:image/${selectedImage!!.extension};base64,"+toBase64(selectedImage.toString())
        }
        
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).editDocProfile(
            "",
            Q.selectedCountry.id.toString(),
            "1",
            img,
            if(edit_fne_txt.text.isNotEmpty())edit_fne_txt.text.toString() else doctorProfileModel!!.firstName_en,
            if(edit_fna_txt.text.isNotEmpty())edit_fna_txt.text.toString() else doctorProfileModel!!.firstName_ar,
            if(edit_lne_txt.text.isNotEmpty())edit_lne_txt.text.toString() else doctorProfileModel!!.lastName_en,
            if(edit_lna_txt.text.isNotEmpty())edit_lna_txt.text.toString() else doctorProfileModel!!.lastName_ar,
            ssList,
            "1",
            "1",
            doctorProfileModel!!.profissionalTitle_en,
            doctorProfileModel!!.profissionalTitle_ar,
            if(edit_about_doc_ar_txt.text.isNotEmpty())edit_about_doc_ar_txt.text.toString() else doctorProfileModel!!.aboutDoctor_ar,
            if(edit_about_doc_en_txt.text.isNotEmpty())edit_about_doc_en_txt.text.toString() else doctorProfileModel!!.aboutDoctor_en,
            "",
            "",
            if(edit_price_txt.text.isNotEmpty())edit_price_txt.text.toString() else doctorProfileModel!!.price.toString(),            "01:03:00",
            if(edit_num_of_days_txt.text.isNotEmpty())edit_num_of_days_txt.text.toString() else doctorProfileModel!!.num_of_day.toString(),

            )
            .enqueue(object : Callback<BaseResponce<DoctorProfileModel>> {
                override fun onFailure(call: Call<BaseResponce<DoctorProfileModel>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<DoctorProfileModel>>,
                    response: Response<BaseResponce<DoctorProfileModel>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            onObserveSuccess()
                            response.body()!!.data!!.let {
                                doctorProfileModel=it
                                setProfileData(it)
                            }
                        } else {

                            onObservefaled()
                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }
    private fun onObserveStart() {
        progrss_lay?.visibility = View.VISIBLE
        offer_lay?.visibility = View.GONE
    }

    private fun onObserveSuccess() {
        progrss_lay?.visibility = View.GONE
        offer_lay?.visibility = View.VISIBLE
    }
    private fun onObservefaled() {
        progrss_lay?.visibility = View.VISIBLE
        offer_lay?.visibility = View.GONE
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
    }
   private fun onEditAddressClicked(){
       edit_address_btn.setOnClickListener {
           intent= Intent(this,MapsActivity::class.java)
           startActivity(intent)
       }
   }
    private fun onEditPasswordClicked(){
        edit_password_btn.setOnClickListener {
            intent= Intent(this,EditPasswordActivity::class.java)
            startActivity(intent)
        }
    }
   private fun fillProfileData(){
       edit_fna_txt.setText(doctorProfileModel!!.firstName_ar)
       edit_fne_txt.setText(doctorProfileModel!!.firstName_en)
       edit_lna_txt.setText(doctorProfileModel!!.lastName_ar)
       edit_lne_txt.setText(doctorProfileModel!!.lastName_ar)
       edit_price_txt.setText(doctorProfileModel!!.price.toString())
       edit_num_of_days_txt.setText(doctorProfileModel!!.num_of_day.toString())
       waiting_time_spinner.setSelection(4)
       edit_about_doc_ar_txt.setText(doctorProfileModel!!.aboutDoctor_ar.toString())
       edit_about_doc_en_txt.setText(doctorProfileModel!!.aboutDoctor_en.toString())
       prof_details__spinner.setSelection(profDetailsList.indexOf(doctorProfileModel!!.profissionalDetails_id.toString()))
       GlideObject.GlideProfilePic(this, doctorProfileModel!!.featured, edit_doc_profile_img)







   }

}