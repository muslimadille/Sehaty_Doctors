package com.muslim_adel.sehatydoctors.modules.offers

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.base.BaseActivity
import com.muslim_adel.sehatydoctors.modules.home.MainActivity
import com.muslim_adel.sehatydoctors.remote.apiServices.ApiClient
import com.muslim_adel.sehatydoctors.remote.apiServices.SessionManager
import com.muslim_adel.sehatydoctors.remote.objects.BaseResponce
import com.muslim_adel.sehatydoctors.remote.objects.PharmAddOfferModel
import com.muslim_adel.sehatydoctors.utiles.Q
import kotlinx.android.synthetic.main.activity_add_new_offer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewOfferActivity : BaseActivity() {
    private  val REQUEST_CODE = 13
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
                    addPharmOfferObserver()

                }

            }



        }
    }
    private fun addPharmOfferObserver() {
        var title_ar=title_ar_txt.text.toString()
        var title_en=offer_title_en_txt.text.toString()
        var price=offer_price_txt.text.toString()
        var img=""

        if(title_ar.isNotEmpty()&&title_en.isNotEmpty()&&price.isNotEmpty()){
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
                                Toast.makeText(
                                    this@AddNewOfferActivity,
                                    "SUCCESS",
                                    Toast.LENGTH_SHORT
                                ).show()
                                intent = Intent(this@AddNewOfferActivity, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@AddNewOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
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
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
               if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                   val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                   requestPermissions(permissions, PERMISSION_CODE)
               } else{
                   chooseImageGallery();
               }
           }else{
               chooseImageGallery();
           }
       }
   }
    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery()
                }else{
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            offer_img.setImageURI(data?.data)
        }
    }
    // companion object
    companion object {
        private val IMAGE_CHOOSE = 1000;
        private val PERMISSION_CODE = 1001;
    }



}