package com.muslim_adel.enaya_doctor.modules.home.schedual.addReservation

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.remote.objects.Date
import com.muslim_adel.enaya_doctor.remote.objects.Dates
import com.muslim_adel.enaya_doctor.remote.objects.Laboratory
import com.muslim_adel.enaya_doctor.utiles.Q
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

        when(preferences!!.getString(Q.USER_TYPE,"")){
            Q.USER_DOCTOR->{
                doctorDateObserver()
            }
            Q.USER_LAB->{
                labObserver()
            }
            Q.USER_PHARM->{}

        }
        initRVAdapter()

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
                                        if(date.status==1&&date.times.isNotEmpty()){
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
    private fun labObserver() {
        docId=intent.getIntExtra("doc_id",0)
        val url = Q.GET_LAB_BY_ID_API +"/${docId}"
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).fitchLabById(url)
            .enqueue(object : Callback<BaseResponce<Laboratory>> {
                override fun onFailure(call: Call<BaseResponce<Laboratory>>, t: Throwable) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<Laboratory>>,
                    response: Response<BaseResponce<Laboratory>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {


                                it.dates.forEach {date:Date ->
                                    if(date.status==1){
                                        doctorDatesList.add(date)
                                    }
                                }
                                doctorDatesListAddapter!!.notifyDataSetChanged()
                                Toast.makeText(this@RservationDatesActivity, "success", Toast.LENGTH_SHORT).show()

                            }
                        } else {
                            Toast.makeText(this@RservationDatesActivity, "faid", Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        Toast.makeText(this@RservationDatesActivity, "connect faid", Toast.LENGTH_SHORT).show()

                    }

                }


            })
    }
}