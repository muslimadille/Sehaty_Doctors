package com.seha_khanah_doctors.modules.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.activity_contact_us.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsActivity : BaseActivity() {
    private var name=""
    private var email=""
    private var phone=""
    private var comment=""
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        contactus_phone_num_btn.let {  it.setText(Q.CONTACT_US_PHONE.toString())}
        onSendClicked()
    }
    private fun messageSend() {

        if (message_tf.text.isNotEmpty() ) {
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).sendContactUs(name, phone,email,comment)
                .enqueue(object : Callback<BaseResponce<Any>> {
                    override fun onFailure(call: Call<BaseResponce<Any>>, t: Throwable) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<Any>>,
                        response: Response<BaseResponce<Any>>
                    ) {

                        val registerResponse = response.body()
                        if (registerResponse!!.success) {
                            onObserveSuccess()
                            Toast.makeText(this@ContactUsActivity, "تم ارسال رسالتك بنجاح", Toast.LENGTH_SHORT).show()
                        } else {
                            onObservefaled()
                            Toast.makeText(
                                this@ContactUsActivity,
                                "فشل العملية حاول مرة أخرى",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        } else {
            Toast.makeText(
                this@ContactUsActivity,
                "من فضلك أكمل البيانات ",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
    private fun onSendClicked(){
        send_message_btn.setOnClickListener {
            if(message_tf.text.toString().isNotEmpty()){
                validateData()
            }else{
                Toast.makeText(this, "قم بإدخال رسالة", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private  fun validateData(){
        name= "user"
        email= "user@email.com"
        phone= "07703454657"
        comment=message_tf.text.toString()
        messageSend()
    }
    private fun onObserveStart() {
        progrss_lay.visibility = View.VISIBLE
    }

    private fun onObserveSuccess() {
        progrss_lay.visibility = View.GONE
    }

    private fun onObservefaled() {
        progrss_lay.visibility = View.GONE
    }

}