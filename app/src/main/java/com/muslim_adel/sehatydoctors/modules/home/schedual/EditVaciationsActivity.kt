package com.muslim_adel.sehatydoctors.modules.home.schedual

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.remote.objects.doctor.DoctorProfileModel
import com.seha_khanah_doctors.remote.objects.doctor.VacancyModel
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.activity_edit_vaciations.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EditVaciationsActivity : BaseActivity() {
    lateinit var dpd: DatePickerDialog
    var vacancyModel:VacancyModel?=null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_vaciations)
        vacancyModel= VacancyModel(0,"","",0)
        pickStartDate()
        pickEndDate()
        onSaveClicked()
    }
    private fun pickStartDate(){
        sat_start_time_txt.setOnClickListener {
            var calendar= Calendar.getInstance()
            var year=calendar.get(Calendar.YEAR)
            var month=calendar.get(Calendar.MONTH)
            var day=calendar.get(Calendar.DAY_OF_MONTH)

            dpd= DatePickerDialog(this,
                { view, myear, mMonth, mdayOfMonth ->

                    var month = ""
                    var day = ""
                    if (mMonth < 10) {
                        month = "0${mMonth+1}"
                    } else {
                        month = "${mMonth+1}"
                    }
                    if (mdayOfMonth < 10) {
                        day = "0$mdayOfMonth"
                    } else {
                        day = "$mdayOfMonth"
                    }
                    vacancyModel!!.start_date = "$myear-$month-$day"
                    val selectedate = SimpleDateFormat("yyyy-MM-dd").parse(vacancyModel!!.start_date)
                    var dayname= SimpleDateFormat("EEEE").format(selectedate)
                    sat_start_time_txt.text="$dayname ${vacancyModel!!.start_date}"



                }, year, month, day
            )
            dpd.show()


        }

    }
    private fun pickEndDate(){
        sat_end_time_txt.setOnClickListener {
            var calendar= Calendar.getInstance()
            var year=calendar.get(Calendar.YEAR)
            var month=calendar.get(Calendar.MONTH)
            var day=calendar.get(Calendar.DAY_OF_MONTH)

            dpd= DatePickerDialog(this,
                { view, myear, mMonth, mdayOfMonth ->

                    var month = ""
                    var day = ""
                    if (mMonth < 10) {
                        month = "0${mMonth+1}"
                    } else {
                        month = "${mMonth+1}"
                    }
                    if (mdayOfMonth < 10) {
                        day = "0$mdayOfMonth"
                    } else {
                        day = "$mdayOfMonth"
                    }
                    vacancyModel!!.end_date = "$myear-$month-$day"
                    val selectedate = SimpleDateFormat("yyyy-MM-dd").parse(vacancyModel!!.start_date)
                    var dayname= SimpleDateFormat("EEEE").format(selectedate)
                    sat_end_time_txt.text="$dayname ${vacancyModel!!.end_date}"



                }, year, month, day
            )
            dpd.show()


        }

    }
    private fun onObserveStart() {
        edite_add_progrss_lay?.visibility = View.VISIBLE
    }
    private fun onObserveSuccess() {
        edite_add_progrss_lay?.visibility = View.GONE
    }
    private fun onObservefaled() {
        edite_add_progrss_lay?.visibility = View.VISIBLE
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
    }
    private fun adddocVacationObserver(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).addDocVacation(vacancyModel!!.start_date,vacancyModel!!.end_date)
            .enqueue(object : Callback<BaseResponce<VacancyModel>> {
                override fun onFailure(call: Call<BaseResponce<VacancyModel>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<VacancyModel>>,
                    response: Response<BaseResponce<VacancyModel>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            onObserveSuccess()
                           finish()
                        } else {

                            onObservefaled()
                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }
    private fun addLabVacationObserver(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).addLabVacation(vacancyModel!!.start_date,vacancyModel!!.end_date)
            .enqueue(object : Callback<BaseResponce<VacancyModel>> {
                override fun onFailure(call: Call<BaseResponce<VacancyModel>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<VacancyModel>>,
                    response: Response<BaseResponce<VacancyModel>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            onObserveSuccess()
                            finish()
                        } else {

                            onObservefaled()
                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }

    private fun onSaveClicked(){
        vacation_edit_btn.setOnClickListener {
            when(preferences!!.getString(Q.USER_TYPE, "")){
                Q.USER_DOCTOR -> {
                    adddocVacationObserver()
                }
                Q.USER_LAB -> {
                    addLabVacationObserver()
                }
                Q.USER_PHARM -> {
                }

            }
        }
    }
}