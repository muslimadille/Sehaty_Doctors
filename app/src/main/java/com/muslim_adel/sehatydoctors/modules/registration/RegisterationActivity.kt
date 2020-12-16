package com.seha_khanah_doctors.modules.registration

import com.seha_khanah_doctors.remote.apiServices.SessionManager
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.remote.objects.LoginData
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.activity_registeration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RegisterationActivity : BaseActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var gender=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)
        datePicker()
        onRegisterClicked()
        handelRdioStates()

    }
    private fun handelRdioStates(){
        gender_male.isChecked=true
        gender=1
        gender_male.setOnClickListener {
            gender_male.isChecked=true
            gender_female.isChecked=false
            gender=1
            if(gender_male.isChecked){
                gender=1
                gender_female.isChecked=false
            }else if(gender_female.isChecked){
                gender=2
                gender_male.isChecked=false
            }
        }
        gender_female.setOnClickListener {
            gender_female.isChecked=true
            gender_male.isChecked=false
            gender=2
            if(gender_male.isChecked){
                gender=1
                gender_female.isChecked=false
            }else if(gender_female.isChecked){
                gender=2
                gender_male.isChecked=false
            }
        }

    }

    private fun onRegisterClicked(){
        register_btn.setOnClickListener {
            if (username.text.isNotEmpty()&&phon_num.text.isNotEmpty()&&email.text.isNotEmpty()&&password.text.isNotEmpty()&&date_of_birth.text.isNotEmpty()&& gender!=-1){
                onObserveStart()
                apiClient = ApiClient()
                sessionManager = SessionManager(this)
                apiClient.getApiService(this).userregister(username.text.toString(),email.text.toString(),password.text.toString(),phon_num.text.toString(),date_of_birth.text.toString(),gender.toString())
                    .enqueue(object : Callback<BaseResponce<LoginData>> {
                        override fun onFailure(call: Call<BaseResponce<LoginData>>, t: Throwable) {
                            alertNetwork(true)
                        }

                        override fun onResponse(
                            call: Call<BaseResponce<LoginData>>,
                            response: Response<BaseResponce<LoginData>>
                        ) {
                            val registerResponse = response.body()
                            if (registerResponse!!.success) {
                                val s = response.body()!!.data.toString()
                                //Toast.makeText(this@RegisterationActivity, s, Toast.LENGTH_SHORT).show()
                                if (registerResponse?.data!!.status == 200 && registerResponse.data!!.user!=null) {
                                    sessionManager.saveAuthToken(registerResponse.data!!.token)
                                    preferences!!.putBoolean(Q.IS_FIRST_TIME,false)
                                    preferences!!.putBoolean(Q.IS_LOGIN,true)
                                    preferences!!.putInteger(Q.USER_ID,registerResponse.data!!.user.id.toInt())
                                    preferences!!.commit()
                                    onObserveSuccess()
                                    val intent =
                                        Intent(this@RegisterationActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    onObservefaled()
                                    Toast.makeText(
                                        this@RegisterationActivity,
                                        "البريد الالكتروني مستخدم من قبل",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                //username.text.clear()
                                //login_password.text.clear()
                                onObservefaled()
                                Toast.makeText(
                                    this@RegisterationActivity,
                                    "فشل في التسجبل",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }


                    })
            }else{
                Toast.makeText(
                    this@RegisterationActivity,
                    "من فضلك أكمل البيانات ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    fun datePicker(){
        val textView: TextView = findViewById(R.id.date_of_birth)
        //textView.text = SimpleDateFormat("yyyy-dd-MM").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-dd-MM" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            textView.setText(sdf.format(cal.time))

        }

        date_lay.setOnClickListener {
            DatePickerDialog(this@RegisterationActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
    private fun onObserveStart(){
        register_progrss_lay.visibility= View.VISIBLE
        register_lay.visibility= View.GONE
    }
    private fun onObserveSuccess(){
        register_progrss_lay.visibility= View.GONE
        register_lay.visibility= View.VISIBLE
    }
    private fun onObservefaled(){
        register_progrss_lay.visibility= View.GONE
        register_lay.visibility= View.GONE
    }
}