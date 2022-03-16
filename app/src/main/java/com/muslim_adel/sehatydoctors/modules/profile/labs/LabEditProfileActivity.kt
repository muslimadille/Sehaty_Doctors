package com.seha_khanah_doctors.modules.profile.labs

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
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
import com.seha_khanah_doctors.modules.map.EditLocationActivity
import com.seha_khanah_doctors.modules.map.MapsActivity
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.remote.objects.Laboratory
import com.seha_khanah_doctors.utiles.Q
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_doctor_edit_profile.*
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.*
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.doc_updat_btn
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.edit_about_doc_ar_txt
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.edit_about_doc_en_txt
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.edit_address_btn
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.edit_doc_profile_img
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.edit_fna_txt
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.edit_fne_txt
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.edit_lna_txt
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.edit_lne_txt
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.edit_password_btn
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.offer_lay
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.progrss_lay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class LabEditProfileActivity : BaseActivity() {
    private var selectedImage: File? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    var doctorProfileModel: Laboratory? =null

    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_lab_profile_edit)
        onSelectIMageClicked()
        ObserveDoctorProfile()
        onEditAddressClicked()
        onSaveClicked()
        onEditPasswordClicked()
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
                        .start(this@LabEditProfileActivity)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(this@LabEditProfileActivity,
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

   
    private  fun setProfileData(profileModel:Laboratory){

    }
    private fun ObserveDoctorProfile(){
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).fitchLabProfile()
            .enqueue(object : Callback<BaseResponce<Laboratory>> {
                override fun onFailure(call: Call<BaseResponce<Laboratory>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<Laboratory>>,
                    response: Response<BaseResponce<Laboratory>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                doctorProfileModel=it
                                setProfileData(it)
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
        apiClient.getApiService(this).editLabProfile(
            Q.selectedCountry.id.toString(),
            "1",
            img,
            if(edit_lab_name_ar_txt.text.isNotEmpty())edit_lab_name_ar_txt.text.toString() else doctorProfileModel!!.laboratory_name_ar,
            if(edit_lab_name_en_txt.text.isNotEmpty())edit_lab_name_en_txt.text.toString() else doctorProfileModel!!.laboratory_name_en,
            if(edit_fne_txt.text.isNotEmpty())edit_fne_txt.text.toString() else doctorProfileModel!!.firstName_en,
            if(edit_fna_txt.text.isNotEmpty())edit_fna_txt.text.toString() else doctorProfileModel!!.firstName_ar,
            if(edit_lne_txt.text.isNotEmpty())edit_lne_txt.text.toString() else doctorProfileModel!!.lastName_en,
            if(edit_lna_txt.text.isNotEmpty())edit_lna_txt.text.toString() else doctorProfileModel!!.lastName_ar,
            if(edit_about_doc_ar_txt.text.isNotEmpty())edit_about_doc_ar_txt.text.toString() else "about",
            if(edit_about_doc_en_txt.text.isNotEmpty())edit_about_doc_en_txt.text.toString() else "about",
            "",
            "",
            if(edit_lab_num_of_days_txt.text.isNotEmpty())edit_lab_num_of_days_txt.text.toString() else doctorProfileModel!!.num_of_day.toString(),
            )
            .enqueue(object : Callback<BaseResponce<Laboratory>> {
                override fun onFailure(call: Call<BaseResponce<Laboratory>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<Laboratory>>,
                    response: Response<BaseResponce<Laboratory>>
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
            intent= Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
    private fun onEditPasswordClicked(){
        edit_password_btn.setOnClickListener {
            intent= Intent(this, EditPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}