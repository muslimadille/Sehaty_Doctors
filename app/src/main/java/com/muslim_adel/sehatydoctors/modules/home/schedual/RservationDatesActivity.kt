package com.muslim_adel.sehatydoctors.modules.home.schedual

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.base.BaseActivity
import com.muslim_adel.sehatydoctors.remote.apiServices.ApiClient
import com.muslim_adel.sehatydoctors.remote.apiServices.SessionManager
import com.muslim_adel.sehatydoctors.remote.objects.BaseResponce
import com.muslim_adel.sehatydoctors.remote.objects.Date
import com.muslim_adel.sehatydoctors.remote.objects.Dates
import com.muslim_adel.sehatydoctors.utiles.Q
import kotlinx.android.synthetic.main.activity_rservation_dates.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RservationDatesActivity : BaseActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var doctorDatesList: MutableList<Date> = ArrayList()
    private var doctorDatesListAddapter: ReservationDatesAdapter? = null
    private var docId=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rservation_dates)
        initRVAdapter()
        doctorDateObserver()
    }
    private fun initRVAdapter() {
        val layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        resevation_dates_rv.layoutManager = layoutManager
        doctorDatesListAddapter = ReservationDatesAdapter(this, doctorDatesList)
        resevation_dates_rv.adapter = doctorDatesListAddapter
    }
    private fun doctorDateObserver() {
        docId=intent.getIntExtra("doc_id",0)
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        val url = Q.DOCTORS_DATES_API + "/${docId}"
        apiClient.getApiService(this).fitchDoctorDatesList(url)
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
                                    it.forEach {date:Date->
                                        if(date.status==1){
                                            doctorDatesList.add(date)
                                        }
                                    }
                                    doctorDatesListAddapter!!.notifyDataSetChanged()
                                } else {
                                    Toast.makeText(
                                        this@RservationDatesActivity,
                                        "data empty",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                            }
                        } else {
                            Toast.makeText(this@RservationDatesActivity, "faild", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@RservationDatesActivity, "faild", Toast.LENGTH_SHORT).show()
                    }

                }


            })
    }
}