package com.muslim_adel.sehatydoctors.modules.home.schedual.addReservation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.base.BaseActivity
import com.muslim_adel.sehatydoctors.modules.home.MainActivity
import com.muslim_adel.sehatydoctors.remote.apiServices.ApiClient
import com.muslim_adel.sehatydoctors.remote.apiServices.SessionManager
import com.muslim_adel.sehatydoctors.remote.objects.BaseResponce
import com.muslim_adel.sehatydoctors.remote.objects.Booking
import com.muslim_adel.sehatydoctors.utiles.Q
import kotlinx.android.synthetic.main.activity_add_new_reservation.*
import org.koin.ext.isInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewReservationActivity : BaseActivity() {
    var selected_date=""
    var date_name=""
    var selected_time=""
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_reservation)
        setProfilrData()
        getIntentValues()
        onBookingClicked()
    }
    private fun getIntentValues() {
        selected_date= intent.getStringExtra("selected_date")!!
        date_name=intent.getStringExtra("date_name")!!
        selected_time=intent.getStringExtra("selected_time")!!


    }
    private fun setProfilrData(){
        getIntentValues()

        if (preferences!!.getString("language","")=="Arabic"){
            date_name_txt.text=date_name
            time_txt.text=selected_time
        }else{
            date_name_txt.text=date_name
            time_txt.text=selected_time
        }

    }
    private fun onBookingClicked(){
        booking_btn.setOnClickListener {
           // if(key==1){
               // offerDateObserver()

           // }else{
                doctorDateObserver()
           // }
        }
    }
    private fun doctorDateObserver() {
        var booking_date="${selected_date} ${selected_time}"
        var name=username.text.toString()
        var phone=phone_num.text.toString()
        var email=mail_txt.text.toString()

        if(!name.isInt()&&name.isNotEmpty()&&phone.isInt()&&phone.length==10){
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).sendBook(name,email,phone,booking_date)
                .enqueue(object : Callback<BaseResponce<Booking>> {
                    override fun onFailure(call: Call<BaseResponce<Booking>>, t: Throwable) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<Booking>>,
                        response: Response<BaseResponce<Booking>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                Toast.makeText(this@AddNewReservationActivity, "SUCCESS", Toast.LENGTH_SHORT).show()
                                intent= Intent(this@AddNewReservationActivity,MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@AddNewReservationActivity, "faild", Toast.LENGTH_SHORT).show()
                            }


                        } else {
                            Toast.makeText(this@AddNewReservationActivity, "faild", Toast.LENGTH_SHORT).show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
}