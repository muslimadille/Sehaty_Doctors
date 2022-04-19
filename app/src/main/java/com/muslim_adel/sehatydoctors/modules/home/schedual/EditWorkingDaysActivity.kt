package com.seha_khanah_doctors.modules.home.schedual


import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.sehatydoctors.modules.home.schedual.EditWorkDaysAdapter
import com.muslim_adel.sehatydoctors.remote.objects.AllTimeModel
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.remote.objects.doctor.DurationModel
import com.seha_khanah_doctors.remote.objects.doctor.WorkingDatesModel
import com.seha_khanah_doctors.remote.objects.doctor.workingTimeModel
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.activity_add_dpctor_offer.*
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.activity_edit_working_days.*
import kotlinx.android.synthetic.main.fragment_appointments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditWorkingDaysActivity : BaseActivity() {

    /** to init an store active days*/
    var workingHoursList: MutableList<WorkingDatesModel> = ArrayList()
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var editWorkDaysAdapter: EditWorkDaysAdapter? = null
    var allTimesList: MutableList<workingTimeModel> = ArrayList()
    var allDurationsList: MutableList<DurationModel> = ArrayList()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_working_days)
        initRVAdapter()
        allTimesObserver()
        allDurationObserver()
        workingDatesObserver()
        onSaveClicked()
    }
    private fun onSaveClicked(){
        edit_working_dates_save_btn.setOnClickListener {
            updateTimes()
        }
    }

    private fun workingDatesObserver() {
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).doctorWorkingDates()
            .enqueue(object : Callback<BaseResponce<List<WorkingDatesModel>>> {
                override fun onFailure(
                    call: Call<BaseResponce<List<WorkingDatesModel>>>,
                    t: Throwable
                ) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<List<WorkingDatesModel>>>,
                    response: Response<BaseResponce<List<WorkingDatesModel>>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                if (it.isNotEmpty()) {
                                    it.forEach {date: WorkingDatesModel ->
                                            workingHoursList.add(date)
                                    }
                                    editWorkDaysAdapter!!.notifyDataSetChanged()

                                } else {
                                    Toast.makeText(this@EditWorkingDaysActivity, "faild", Toast.LENGTH_SHORT).show()
                                }

                            }
                        } else {
                            Toast.makeText(this@EditWorkingDaysActivity, "faild", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@EditWorkingDaysActivity, "faild", Toast.LENGTH_SHORT).show()
                    }

                }


            })
    }
    private fun allTimesObserver() {
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).fitchAllTimesList()
            .enqueue(object : Callback<BaseResponce<List<workingTimeModel>>> {
                override fun onFailure(
                    call: Call<BaseResponce<List<workingTimeModel>>>,
                    t: Throwable
                ) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<List<workingTimeModel>>>,
                    response: Response<BaseResponce<List<workingTimeModel>>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                if (it.isNotEmpty()) {
                                    it.forEach {date: workingTimeModel ->
                                        allTimesList.add(date)
                                    }
                                    editWorkDaysAdapter!!.notifyDataSetChanged()

                                } else {
                                    Toast.makeText(this@EditWorkingDaysActivity, "faild", Toast.LENGTH_SHORT).show()
                                }

                            }
                        } else {
                            Toast.makeText(this@EditWorkingDaysActivity, "faild", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@EditWorkingDaysActivity, "faild", Toast.LENGTH_SHORT).show()
                    }

                }


            })
    }
    private fun allDurationObserver() {
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).fitchDurationList()
            .enqueue(object : Callback<BaseResponce<List<DurationModel>>> {
                override fun onFailure(
                    call: Call<BaseResponce<List<DurationModel>>>,
                    t: Throwable
                ) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<List<DurationModel>>>,
                    response: Response<BaseResponce<List<DurationModel>>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                if (it.isNotEmpty()) {
                                    it.forEach {date: DurationModel ->
                                        allDurationsList.add(date)
                                    }
                                    editWorkDaysAdapter!!.notifyDataSetChanged()

                                } else {
                                    Toast.makeText(this@EditWorkingDaysActivity, "faild", Toast.LENGTH_SHORT).show()
                                }

                            }
                        } else {
                            Toast.makeText(this@EditWorkingDaysActivity, "faild", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@EditWorkingDaysActivity, "faild", Toast.LENGTH_SHORT).show()
                    }

                }


            })
    }

    private fun initRVAdapter() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        edit_work_days_rv.layoutManager = layoutManager
        editWorkDaysAdapter = EditWorkDaysAdapter(this,workingHoursList,allTimesList,allDurationsList)
        edit_work_days_rv.adapter = editWorkDaysAdapter
    }
    private fun updateTimes() {
         var times: MutableMap<String, Int> = emptyMap<String,Int>().toMutableMap()
        for(i in 0 until workingHoursList.size){
            times["working_hour[${i}][day_id]"]=workingHoursList[i].day_id
            times["working_hour[${i}][time_from_id]"]=workingHoursList[i].time_from_id
            times["working_hour[${i}][time_to_id]"]=workingHoursList[i].time_to_id
            times["working_hour[${i}][duration_id]"]=workingHoursList[i].duration_id
            times["working_hour[${i}][status]"]=workingHoursList[i].status
        }
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
        print("${apiClient.getApiService(this).docUpdateWorkingTime(times).request()}")
            apiClient.getApiService(this).docUpdateWorkingTime(times)
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
                            Toast.makeText(this@EditWorkingDaysActivity, "تم تعديل البيانات بنجاح", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                this@EditWorkingDaysActivity,
                                "فشل العملية حاول مرة أخرى",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })


    }


}