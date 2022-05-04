package com.muslim_adel.enaya_doctor.modules.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.muslim_adel.enaya_doctor.remote.objects.Verification
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import kotlinx.android.synthetic.main.activity_verivication.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerivicationActivity : BaseActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var phone =""
    private var type =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verivication)
        phone="+"+intent.getStringExtra("phone")!!
        type=intent.getStringExtra("type")!!
        sendVerOrder()
        onSendClicked()
    }
    private fun onObserveStart() {
        ver_progrss_lay.visibility = View.VISIBLE
    }

    private fun onObserveSuccess() {
        ver_progrss_lay.visibility = View.GONE
    }

    private fun onObservefaled() {
        ver_progrss_lay.visibility = View.GONE
    }
    private fun sendVerOrder(){
        retry_btn.setOnClickListener {
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this)
                .userVerification(phone, type)
                .enqueue(object : Callback<BaseResponce<Verification>> {
                    override fun onFailure(call: Call<BaseResponce<Verification>>, t: Throwable) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<Verification>>,
                        response: Response<BaseResponce<Verification>>
                    ) {
                        val loginResponse = response.body()
                        if (loginResponse!!.success) {
                            onObserveSuccess()
                        } else {
                            onObservefaled()
                            Toast.makeText(
                                this@VerivicationActivity,
                                "خطا حاول مرة أخري",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }


                })
        }

    }
    private  fun sendNumber(){
        if (message_tf.text.isNotEmpty()){
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).sendVerificationNum(phone, type,message_tf.text.toString())
                .enqueue(object : Callback<BaseResponce<Verification>> {
                    override fun onFailure(call: Call<BaseResponce<Verification>>, t: Throwable) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<Verification>>,
                        response: Response<BaseResponce<Verification>>
                    ) {
                        val registerResponse = response.body()
                        if (registerResponse!!.success) {
                            onObserveSuccess()
                            val intent =
                                Intent(this@VerivicationActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            //username.text.clear()
                            //login_password.text.clear()
                            onObservefaled()
                            Toast.makeText(
                                this@VerivicationActivity,
                                "فشل في التسجبل",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }


                })
        }else{
            Toast.makeText(
                this@VerivicationActivity,
                "من فضلك أدخل رقم صحيح ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun onSendClicked(){
        send_message_btn.setOnClickListener {
            sendNumber()
        }
    }
}