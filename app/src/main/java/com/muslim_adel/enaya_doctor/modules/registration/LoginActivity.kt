package com.muslim_adel.enaya_doctor.modules.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.muslim_adel.enaya_doctor.modules.newRegistration.doctor.DoctorRegistrationScreen
import com.muslim_adel.enaya_doctor.modules.newRegistration.labs.LabRegisterationActivity
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
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
    override fun onCreate(savedInstanceState: Bundle?) {
        key=intent.getIntExtra("key",0)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        onregisterclicked()
        onloginclicked()
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
                    val intent = Intent(this@LoginActivity, DoctorRegistrationScreen::class.java)
                    intent.putExtra("key",3)
                    startActivity(intent)
                    /*val intent = Intent(this@LoginActivity, PharmRegistrationActivity::class.java)
                    startActivity(intent)*/
                }
            }


        }
    }
    private fun dctorLogin(){
        if (username.text.isNotEmpty() && login_password.text.isNotEmpty()) {
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            Log.d("APIS","${
                apiClient.getApiService(this)
                    .login("${Q.selectedCountry.phoneCode}"+username.text.toString(), login_password.text.toString()).request()
            }")
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


                })
        }
    }

}