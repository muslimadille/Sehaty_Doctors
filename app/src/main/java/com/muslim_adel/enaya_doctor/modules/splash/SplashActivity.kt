package com.muslim_adel.enaya_doctor.modules.splash

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.muslim_adel.enaya_doctor.modules.settings.ContactUsModel
import com.muslim_adel.enaya_doctor.remote.objects.CountryModel
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.modules.registration.SelectUserActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.utiles.Q
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SplashActivity : BaseActivity() {
    private var change=""
    private var isLogin=false
    var isFristTime=true
    private lateinit var referance: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        database = FirebaseDatabase.getInstance()
        referance=database.getReference("state")
        isLogin=preferences!!.getBoolean(Q.IS_LOGIN,false)
        isFristTime=preferences!!.getBoolean(Q.IS_FIRST_TIME,true)
        isFristTime=preferences!!.getBoolean(Q.IS_FIRST_TIME, Q.FIRST_TIME)
        setLocalization()
        getCountries()
        getContactUsData()


    }



    fun getCountries(){
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this)
            .getAllCountriesList()
            .enqueue(object : Callback<BaseResponce<List<CountryModel>>> {
                override fun onFailure(call: Call<BaseResponce<List<CountryModel>>>, t: Throwable) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<List<CountryModel>>>,
                    response: Response<BaseResponce<List<CountryModel>>>
                ) {
                    val myResponse = response.body()
                    if (myResponse!!.success) {
                        Q.countriesList.clear()
                        Q.countriesList.addAll(myResponse.data!!)
                        Q.selectedCountry=Q.countriesList[0]
                        handelSpalash()

                    } else {

                        Toast.makeText(
                            this@SplashActivity,
                            "Error:${myResponse.data}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }


            })
    }
    fun getContactUsData(){
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this)
            .contactUsData()
            .enqueue(object : Callback<BaseResponce<List<ContactUsModel>>> {
                override fun onFailure(call: Call<BaseResponce<List<ContactUsModel>>>, t: Throwable) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<List<ContactUsModel>>>,
                    response: Response<BaseResponce<List<ContactUsModel>>>
                ) {
                    val myResponse = response.body()
                    if (myResponse!!.success) {
                        Q.CONTACT_US_PHONE=myResponse.data!![0].phonenum
                    } else {

                        Toast.makeText(
                            this@SplashActivity,
                            "Error:${myResponse.data}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }


            })
    }


    private fun setLocalization(){
        val language = preferences!!.getString("language", "Arabic")
        if (language =="Arabic") {
            change="ar"
            Q.CURRENT_LANG="ar"
        } else if (language=="English" ) {
            change = "en"
            Q.CURRENT_LANG="en"
        }else if(language=="Kurdish") {
            change ="ur"
            Q.CURRENT_LANG="ur"
        }
        dLocale = Locale(change) //set any locale you want here

    }
    private fun handelSpalash(){

        Handler().postDelayed({
            if (isFristTime) {
                preferences!!.putString("language", "Arabic")
                preferences!!.commit()
                val intent = Intent(this@SplashActivity, SelectUserActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                if(isLogin){
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this@SplashActivity, SelectUserActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 2000)

    }
    private fun getState(){
        referance.get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            if(it.value=="2291848"){

            }else{
                val intent=Intent(this, NoActivity::class.java)
                startActivity(intent)
            }

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
    private  fun getCuntryData(){
        Q.PHONE_KEY="+964"
        Q.CURNCY_NAME_AR="دينار"
    }
}