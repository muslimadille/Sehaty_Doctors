package com.seha_khanah_doctors.modules.home.schedual

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.sehatydoctors.modules.home.schedual.EditVaciationsActivity
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.modules.profile.doctor.VacationDatesAdapter
import com.seha_khanah_doctors.modules.profile.doctor.WorkingDatesAdapter
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.remote.objects.doctor.VacancyModel
import com.seha_khanah_doctors.remote.objects.doctor.WorkingDatesModel
import kotlinx.android.synthetic.main.fragment_appointments.*
import kotlinx.android.synthetic.main.fragment_appointments.all_days_rv
import kotlinx.android.synthetic.main.fragment_appointments_manage.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AppointmentsManageFragment : Fragment() {
    private var workingHoursList: MutableList<WorkingDatesModel> = ArrayList()
    private var workingHoursAddapter: WorkingDatesAdapter? = null

    private var vacationsList: MutableList<VacancyModel> = ArrayList()
    private var vacationsAddapter: VacationDatesAdapter? = null

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workingDatesObserver()
        vacationDatesObserver()
        initRVAdapter()
        onEditWorkingDates()
        onEditVacationsClicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointments_manage, container, false)
    }
    private fun workingDatesObserver() {
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).doctorWorkingDates()
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
                                    it.forEach {date:WorkingDatesModel->
                                        if(date.status==1){
                                            workingHoursList.add(date)
                                        }


                                    }
                                    workingHoursAddapter!!.notifyDataSetChanged()

                                } else {
                                    Toast.makeText(mContext, "faild", Toast.LENGTH_SHORT).show()
                                }

                            }
                        } else {
                            Toast.makeText(mContext, "faild", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(mContext, "faild", Toast.LENGTH_SHORT).show()
                    }

                }


            })
    }
    private fun vacationDatesObserver() {
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).doctorVacanciesDates()
            .enqueue(object : Callback<BaseResponce<List<VacancyModel>>> {
                override fun onFailure(
                    call: Call<BaseResponce<List<VacancyModel>>>,
                    t: Throwable
                ) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<List<VacancyModel>>>,
                    response: Response<BaseResponce<List<VacancyModel>>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                if (it.isNotEmpty()) {
                                    it.forEach {date:VacancyModel->
                                            vacationsList.add(date)
                                    }
                                    vacationsAddapter!!.notifyDataSetChanged()

                                } else {
                                    Toast.makeText(mContext, "faild", Toast.LENGTH_SHORT).show()
                                }

                            }
                        } else {
                            Toast.makeText(mContext, "faild", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(mContext, "faild", Toast.LENGTH_SHORT).show()
                    }

                }


            })
    }
    var mContext: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as MainActivity

    }
    fun alertNetwork(isExit: Boolean = true) {
        val alertBuilder = AlertDialog.Builder(mContext!!)
        //alertBuilder.setTitle(R.string.error)
        alertBuilder.setMessage(R.string.no_internet)
        if (isExit) {
            // alertBuilder.setPositiveButton(R.string.exit) { dialog: DialogInterface, _: Int -> context!!.finish() }
        } else {
            alertBuilder.setPositiveButton(R.string.dismiss) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        }
        if (!mContext!!.isFinishing){
            alertBuilder.show()
        }
    }
    private fun initRVAdapter() {
        val layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        days_duration_rv.layoutManager = layoutManager
        workingHoursAddapter = WorkingDatesAdapter(mContext!!,workingHoursList)
        days_duration_rv.adapter = workingHoursAddapter

        val layoutManager2 = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        vacations_rv.layoutManager = layoutManager2
        vacationsAddapter = VacationDatesAdapter(mContext!!,vacationsList)
        vacations_rv.adapter = vacationsAddapter
    }
   private  fun onEditWorkingDates(){
       edit_working_dates_btn.setOnClickListener {
           mContext!!.intent= Intent(mContext,EditWorkingDaysActivity::class.java)
           mContext!!.startActivity(mContext!!.intent)
       }
   }
    private fun onEditVacationsClicked(){
        edit_vacation_btn.setOnClickListener {
            mContext!!.intent=Intent(mContext,EditVaciationsActivity::class.java)
            mContext!!.startActivity(mContext!!.intent)
        }
    }

    
}