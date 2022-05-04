package com.muslim_adel.enaya_doctor.modules.offers

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.base.GlideObject
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.remote.objects.PharmAddOfferModel
import com.muslim_adel.enaya_doctor.utiles.IOUtile
import com.muslim_adel.enaya_doctor.utiles.Q
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_new_offer.*
import kotlinx.android.synthetic.main.activity_add_new_offer.add_offer_btn
import kotlinx.android.synthetic.main.fragment_offers.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Base64


class AddNewOfferActivity : BaseActivity() {
    private var selectedImage: File? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_offer)
        onAddOfferClicked()
        onSelectIMageClicked()

    }
    private fun onAddOfferClicked(){
        add_offer_btn.setOnClickListener {
            when(preferences!!.getString(Q.USER_TYPE, "")){
                Q.USER_DOCTOR -> {
                }
                Q.USER_LAB -> {
                }
                Q.USER_PHARM -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        addPharmOfferObserver()
                    }

                }

            }



        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addPharmOfferObserver() {
        var img=""
        var title_ar=title_ar_txt.text.toString()
        var title_en=offer_title_en_txt.text.toString()
        var price=offer_price_txt.text.toString()
        if (selectedImage != null) {
            val fileInBytes = IOUtile.readFileAsByteArray(selectedImage!!)
            img = "data:image/${selectedImage!!.extension};base64,"+toBase64(selectedImage.toString())
        }

        if(title_ar.isNotEmpty()&&title_en.isNotEmpty()&&price.isNotEmpty()){
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).addPharmOffer(title_ar, title_en, price, img)
                .enqueue(object : Callback<BaseResponce<PharmAddOfferModel>> {
                    override fun onFailure(
                        call: Call<BaseResponce<PharmAddOfferModel>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<PharmAddOfferModel>>,
                        response: Response<BaseResponce<PharmAddOfferModel>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                onObserveSuccess()
                                Toast.makeText(
                                    this@AddNewOfferActivity,
                                    "تم إضافة العرض بنجاح",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                onObservefaled()
                                Toast.makeText(
                                    this@AddNewOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            onObservefaled()
                            Toast.makeText(this@AddNewOfferActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
   private fun onSelectIMageClicked(){
       offer_img.setOnClickListener {
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
                        .start(this@AddNewOfferActivity)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(this@AddNewOfferActivity,
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

                    GlideObject.GlideProfilePic(this, selectedImage!!.path, offer_img)
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
    private fun onObserveStart() {
        phar_add_offer_progrss_lay?.visibility = View.VISIBLE
        phar_add_ofer_lay?.visibility = View.GONE
    }
    private fun onObserveSuccess() {
        phar_add_ofer_lay?.visibility = View.VISIBLE
        phar_add_offer_progrss_lay?.visibility = View.GONE
    }
    private fun onObservefaled() {
        phar_add_offer_progrss_lay?.visibility = View.GONE
        phar_add_ofer_lay?.visibility = View.VISIBLE
    }
}