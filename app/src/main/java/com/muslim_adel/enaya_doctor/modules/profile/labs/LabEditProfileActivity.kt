package com.muslim_adel.enaya_doctor.modules.profile.labs

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.muslim_adel.enaya_doctor.modules.profile.edit_password.EditPasswordActivity
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.base.GlideObject
import com.muslim_adel.enaya_doctor.modules.map.MapsActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.remote.objects.Laboratory
import com.muslim_adel.enaya_doctor.utiles.Q
import kotlinx.android.synthetic.main.activity_doctor_edit_profile.*
import kotlinx.android.synthetic.main.activity_pharmacy_edit_profile.*
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
import kotlinx.android.synthetic.main.fragment_registration2.*
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
                    ImagePicker.with(this@LabEditProfileActivity)
                        .compress(1024)         //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
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
    val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    result?.let {
                        selectedImage = File(result!!.data!!.data!!.path!!)
                        GlideObject.GlideProfilePic(this, selectedImage!!.path, edit_doc_profile_img)
//                    Picasso.get().load(selectedImage!!)
                    }
                }


            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64(filePath: String): String{
        val bytes = File(filePath).readBytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)
        return base64
    }

   
    private  fun setProfileData(profileModel:Laboratory){
        edit_lab_name_ar_txt.setText(doctorProfileModel!!.laboratory_name_ar)
        edit_lab_name_en_txt.setText(doctorProfileModel!!.laboratory_name_en)
        edit_fne_txt.setText(doctorProfileModel!!.firstName_en)
        edit_fna_txt.setText(doctorProfileModel!!.firstName_ar)
        edit_lne_txt.setText(doctorProfileModel!!.lastName_en)
        edit_lna_txt.setText(doctorProfileModel!!.lastName_ar)
        edit_about_doc_ar_txt.setText(doctorProfileModel!!.about_ar)
        edit_about_doc_en_txt.setText(doctorProfileModel!!.about_en)
        edit_lab_num_of_days_txt.setText(doctorProfileModel!!.num_of_day.toString())
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
            "",
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
                                ObserveDoctorProfile()
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