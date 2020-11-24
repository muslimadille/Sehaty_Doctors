package com.muslim_adel.sehatydoctors.modules.home.schedual.addReservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.base.BaseActivity
import com.muslim_adel.sehatydoctors.remote.apiServices.ApiClient
import com.muslim_adel.sehatydoctors.remote.apiServices.SessionManager
import com.muslim_adel.sehatydoctors.remote.objects.BaseResponce
import com.muslim_adel.sehatydoctors.remote.objects.Date
import com.muslim_adel.sehatydoctors.remote.objects.Dates
import com.muslim_adel.sehatydoctors.remote.objects.Times
import com.muslim_adel.sehatydoctors.utiles.Q
import kotlinx.android.synthetic.main.activity_reservation_times.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationTimesActivity : BaseActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var timesList: MutableList<Times> = ArrayList()
    private var doctorDatesListAddapter: ReservationTimesAdapter? = null
    var datename=""
    var selecteddate=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_times)
        initRVAdapter()
        doctorDateObserver()
    }
    private fun initRVAdapter() {
        doctorDatesListAddapter = ReservationTimesAdapter(this, timesList)
        val layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        dates_list.layoutManager = layoutManager
        dates_list.adapter = doctorDatesListAddapter
    }
    private fun doctorDateObserver() {
        var dateId=intent.getIntExtra("date_id",0)
        var doctor_id=preferences!!.getInteger(Q.USER_ID,0)
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).fitchDoctorDatesList(Q.DOCTORS_DATES_API + "/${doctor_id}")
            .enqueue(object : Callback<BaseResponce<Dates>> {
                override fun onFailure(call: Call<BaseResponce<Dates>>, t: Throwable) {
                    alertNetwork(true)
                }
                override fun onResponse(
                    call: Call<BaseResponce<Dates>>,
                    response: Response<BaseResponce<Dates>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.dates.let {
                                if (it.isNotEmpty()) {
                                    it.forEach {date: Date ->
                                        if(date.id==dateId){
                                            if (preferences!!.getString("language","")=="Arabic"){
                                                datename=date.day_ar+" "+date.date
                                                day_name.text=date.day_ar+" "+date.date
                                                selecteddate=date.date
                                            }else{
                                                datename=date.day_en+" "+date.date
                                                day_name.text=date.day_en+" "+date.date
                                                selecteddate=date.date
                                            }

                                            date.times.forEach {time:Times->
                                                if(time.status=="1"){
                                                    timesList.add(time)
                                                }
                                            }
                                            doctorDatesListAddapter!!.notifyDataSetChanged()
                                        }
                                    }

                                } else {
                                    Toast.makeText(
                                        this@ReservationTimesActivity,
                                        "data empty",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                            }
                        } else {
                            Toast.makeText(this@ReservationTimesActivity, "faild", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@ReservationTimesActivity, "faild", Toast.LENGTH_SHORT).show()
                    }

                }


            })
    }
}