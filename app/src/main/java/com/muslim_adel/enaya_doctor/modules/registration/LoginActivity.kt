package com.muslim_adel.enaya_doctor.modules.registration

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.muslim_adel.enaya_doctor.modules.newRegistration.doctor.DoctorRegistrationScreen
import com.muslim_adel.enaya_doctor.modules.newRegistration.labs.LabRegisterationActivity
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.PharmRegisterationActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.LaboratoryLoginResponce
import com.muslim_adel.enaya_doctor.remote.objects.LoginResponce
import com.muslim_adel.enaya_doctor.remote.objects.PharmacyLoginResponce
import com.muslim_adel.enaya_doctor.utiles.Q
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
    private var isLogin = false
    private var key=0
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var isGPSopened:Boolean=false

    private lateinit var context: Context
    var intent1: Intent? = null
    private lateinit var locationManager: LocationManager
    var gpsStatus = false
    override fun onCreate(savedInstanceState: Bundle?) {
        key=intent.getIntExtra("key",0)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        onregisterclicked()
        onloginclicked()
        onForgetPWclicked()
        context = applicationContext
    }
    private fun checkGpsStatus() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (gpsStatus) {
            when(key){
                1->{
                    val intent = Intent(this@LoginActivity, DoctorRegistrationScreen::class.java)
                    intent.putExtra("key",1)
                    startActivity(intent)
                    /*val intent = Intent(this@LoginActivity, DoctorRegistrationScreen::class.java)
                    startActivity(intent)*/
                }
                2->{
                    val intent = Intent(this@LoginActivity, LabRegisterationActivity::class.java)
                    intent.putExtra("key",2)
                    startActivity(intent)
                    /* val intent = Intent(this@LoginActivity, LabRegistrationActivity::class.java)
                     startActivity(intent)*/
                }
                3->{
                    val intent = Intent(this@LoginActivity, PharmRegisterationActivity::class.java)
                    intent.putExtra("key",3)
                    startActivity(intent)
                    /*val intent = Intent(this@LoginActivity, PharmRegistrationActivity::class.java)
                    startActivity(intent)*/
                }
            }
        } else {
            Toast.makeText(this,"الرجاء تفعيل تحديد الموقع للمتابعة", Toast.LENGTH_LONG).show()
            intent1 = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1)
        }

    }

    private fun onObserveStart() {
        login_progrss_lay.visibility = View.VISIBLE
    }

    private fun onObserveSuccess() {
        login_progrss_lay.visibility = View.GONE
    }

    private fun onObservefaled() {
        login_progrss_lay.visibility = View.GONE
    }
    private fun onForgetPWclicked(){
        forget_pw_btn.setOnClickListener {


                when(key){
                    1->{
                        val intent =
                            Intent(this@LoginActivity, AddPhoneActivity::class.java)
                        intent.putExtra("type","doctor")
                        startActivity(intent)
                    }
                    2->{
                        val intent =
                            Intent(this@LoginActivity, AddPhoneActivity::class.java)
                        intent.putExtra("type","laboratory")
                        startActivity(intent)
                    }
                    3->{
                        val intent =
                            Intent(this@LoginActivity, AddPhoneActivity::class.java)
                        intent.putExtra("type","pharmacy")

                        startActivity(intent)
                    }
                }

        }

    }
    private fun onloginclicked(){
        Login_btn.setOnClickListener {
            when(key){
                1->{
                    dctorLogin()                }
                2->{
                    labLogin()
                }
                3->{
                    pharmLogin()
                }
            }
        }
    }
    private fun onregisterclicked(){

        registration_btn.setOnClickListener {

            checkGpsStatus()



        }
    }
    private fun dctorLogin(){
        if (username.text.isNotEmpty() && login_password.text.isNotEmpty()) {
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(this)

            apiClient.getApiService(this)
                .login("${Q.selectedCountry.phoneCode}"+username.text.toString(), login_password.text.toString())
                .enqueue(object : Callback<LoginResponce> {
                    override fun onFailure(call: Call<LoginResponce>, t: Throwable) {
                        //alertNetwork(true)
                        Toast.makeText(
                            this@LoginActivity,
                            "كلمة المرور او البريد الالكتروني غير صحيح ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<LoginResponce>,
                        response: Response<LoginResponce>
                    ) {
                        val loginResponse = response.body()
                        if (loginResponse!!.success) {
                            if (loginResponse?.data!!.status == 200 && loginResponse.data.user != null) {
                                username.text.clear()
                                login_password.text.clear()
                                sessionManager.saveAuthToken(loginResponse.data.token,loginResponse!!.data!!.user!!.country_id!!)
                                preferences!!.putString("tok",loginResponse.data.token.toString())
                                preferences!!.putInteger("COUNTRY_ID", loginResponse.data.user.country_id!!)
                                preferences!!.putBoolean(Q.IS_FIRST_TIME, false)
                                preferences!!.putBoolean(Q.IS_LOGIN, true)
                                preferences!!.putString(Q.USER_TYPE,Q.USER_DOCTOR)
                                preferences!!.putString(Q.USER_NAME,loginResponse.data.user.firstName_ar)
                                preferences!!.putString(Q.USER_EMAIL,"doctor@seha.com")
                                preferences!!.putString(Q.USER_PHONE,loginResponse.data.user.phonenumber.toString())

                                preferences!!.putInteger(
                                    Q.USER_ID,
                                    loginResponse.data.user.id.toInt()
                                )

                                preferences!!.commit()
                                onObserveSuccess()
                                val intent =
                                    Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            if(loginResponse.message.toString().contains("not verified")){
                                val intent =
                                    Intent(this@LoginActivity, VerivicationActivity::class.java)
                                intent.putExtra("phone",Q.PHONE_KEY.replace("+","")+username.text)
                                intent.putExtra("type","doctor")
                                startActivity(intent)
                                finish()
                            }
                            else{
                                onObservefaled()
                                username.text.clear()
                                login_password.text.clear()
                                Toast.makeText(
                                    this@LoginActivity,
                                    "كلمة المرور او البريد الالكتروني غير صحيح ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }


                })
        }
    }
    private fun labLogin(){
        if (username.text.isNotEmpty() && login_password.text.isNotEmpty()) {
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this)
                .labLogin("${Q.selectedCountry.phoneCode}"+username.text.toString(), login_password.text.toString())
                .enqueue(object : Callback<LaboratoryLoginResponce> {
                    override fun onFailure(call: Call<LaboratoryLoginResponce>, t: Throwable) {
                       // alertNetwork(true)
                        Toast.makeText(
                            this@LoginActivity,
                            "كلمة المرور او البريد الالكتروني غير صحيح ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    override fun onResponse(
                        call: Call<LaboratoryLoginResponce>,
                        response: Response<LaboratoryLoginResponce>
                    ) {
                        val loginResponse = response.body()
                        if (loginResponse!!.success) {
                            if (loginResponse?.data!!.status == 200 && loginResponse.data.user != null) {
                                username.text.clear()
                                login_password.text.clear()
                                sessionManager.saveAuthToken(loginResponse.data.token,loginResponse!!.data!!.user!!.country_id!!)
                                preferences!!.putString("tok",loginResponse.data.token)
                                preferences!!.putInteger("COUNTRY_ID",loginResponse!!.data!!.user!!.country_id!!)
                                preferences!!.putBoolean(Q.IS_FIRST_TIME, false)
                                preferences!!.putBoolean(Q.IS_LOGIN, true)
                                preferences!!.putString(Q.USER_TYPE,Q.USER_LAB)
                                preferences!!.putString(Q.USER_NAME,loginResponse.data.user.firstName_ar)
                                preferences!!.putString(Q.USER_EMAIL,"doctor@seha.com")
                                preferences!!.putString(Q.USER_PHONE,loginResponse.data.user.phonenumber.toString())

                                preferences!!.putInteger(
                                    Q.USER_ID,
                                    loginResponse.data.user.id.toInt()
                                )

                                preferences!!.commit()
                                onObserveSuccess()
                                val intent =
                                    Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            if(loginResponse.message.toString().contains("not verified")){
                                val intent =
                                    Intent(this@LoginActivity, VerivicationActivity::class.java)
                                intent.putExtra("phone",Q.PHONE_KEY.replace("+","")+username.text)
                                intent.putExtra("type","laboratory")
                                startActivity(intent)
                                finish()
                            }else{
                                onObservefaled()
                                username.text.clear()
                                login_password.text.clear()
                                Toast.makeText(
                                    this@LoginActivity,
                                    "كلمة المرور او البريد الالكتروني غير صحيح ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                    }


                })
        }
    }
    private fun pharmLogin(){
        if (username.text.isNotEmpty() && login_password.text.isNotEmpty()) {
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this)
                .pharmLogin("${Q.selectedCountry.phoneCode}"+username.text.toString(), login_password.text.toString())
                .enqueue(object : Callback<PharmacyLoginResponce> {
                    override fun onFailure(call: Call<PharmacyLoginResponce>, t: Throwable) {
                        //alertNetwork(true)
                        Toast.makeText(
                            this@LoginActivity,
                            "كلمة المرور او البريد الالكتروني غير صحيح ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<PharmacyLoginResponce>,
                        response: Response<PharmacyLoginResponce>
                    ) {
                        val loginResponse = response.body()
                        if (loginResponse!!.success) {
                            if (loginResponse?.data!!.status == 200 && loginResponse.data.user != null) {
                                username.text.clear()
                                login_password.text.clear()
                                sessionManager.saveAuthToken(loginResponse.data.token,loginResponse!!.data!!.user!!.country_id!!)
                                preferences!!.putString("tok",loginResponse.data.token.toString())
                                preferences!!.putInteger("COUNTRY_ID",loginResponse!!.data!!.user!!.country_id!!)
                                preferences!!.putBoolean(Q.IS_FIRST_TIME, false)
                                preferences!!.putBoolean(Q.IS_LOGIN, true)
                                preferences!!.putString(Q.USER_TYPE,Q.USER_PHARM)
                                preferences!!.putInteger(Q.USER_ID, loginResponse.data.user.id.toInt())
                                preferences!!.putString(Q.USER_NAME,loginResponse.data.user.firstName_ar)
                                preferences!!.putString(Q.USER_EMAIL,"doctor@seha.com")
                                preferences!!.putString(Q.USER_PHONE,loginResponse.data.user.phonenumber.toString())

                                preferences!!.commit()
                                onObserveSuccess()
                                val intent =
                                    Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            if(loginResponse.message.toString().contains("not verified")){
                                val intent =
                                    Intent(this@LoginActivity, VerivicationActivity::class.java)
                                intent.putExtra("phone",Q.PHONE_KEY.replace("+","")+username.text)
                                intent.putExtra("type","pharmacy")
                                startActivity(intent)
                                finish()
                            }else{
                                onObservefaled()
                                username.text.clear()
                                login_password.text.clear()
                                Toast.makeText(
                                    this@LoginActivity,
                                    "كلمة المرور او البريد الالكتروني غير صحيح ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                    }


                })
        }
    }

}