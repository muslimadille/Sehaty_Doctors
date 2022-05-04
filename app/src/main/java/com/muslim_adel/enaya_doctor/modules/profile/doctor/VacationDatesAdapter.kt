package com.muslim_adel.enaya_doctor.modules.profile.doctor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.remote.objects.OfferUnitsModel
import com.muslim_adel.enaya_doctor.remote.objects.doctor.VacancyModel
import com.muslim_adel.enaya_doctor.utiles.ComplexPreferences
import com.muslim_adel.enaya_doctor.utiles.Q
import kotlinx.android.synthetic.main.vacation_dates_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VacationDatesAdapter(
    private val mContext: Context,
    private val list: MutableList<VacancyModel>
) : RecyclerView.Adapter<VacationDatesAdapter.ViewHolder>() {
    var preferences: ComplexPreferences? = null
    init {
        preferences = ComplexPreferences.getComplexPreferences(mContext as MainActivity, Q.PREF_FILE, Q.MODE_PRIVATE)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(R.layout.vacation_dates_item, parent, false)
        return ViewHolder(convertView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vacation = list[position]

        holder.vacation_date_from_txt?.let { it.text=vacation.start_date}
        holder.vacation_date_to_txt?.let { it .text=vacation.end_date}
        holder.vacation_delete_btn?.let{ it.setOnClickListener {
            //cal delete item fun

            when(preferences!!.getString(Q.USER_TYPE, "")){
                Q.USER_DOCTOR -> {
                    deleteVacation(vacation.id)

                }
                Q.USER_LAB -> {
                    labDeleteVacation(vacation.id)

                }
                Q.USER_PHARM -> {
                }

            }
        }}

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val vacation_date_from_txt: TextView? =view.vacation_date_from_txt
        val vacation_date_to_txt: TextView? =view.vacation_date_to_txt
        val vacation_delete_btn:ImageView?=view.vacation_delete_btn

    }
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private fun deleteVacation(vacationId:Int) {

        apiClient = ApiClient()
        sessionManager = SessionManager(mContext)
        var url=Q.DELETE_VACATION_API+"/$vacationId"
        apiClient.getApiService(mContext).deleteVacation(url)
            .enqueue(object : Callback<BaseResponce<Any>> {
                override fun onFailure(
                    call: Call<BaseResponce<Any>>,
                    t: Throwable
                ) {

                }

                override fun onResponse(
                    call: Call<BaseResponce<Any>>,
                    response: Response<BaseResponce<Any>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            vacationDatesObserver()
                        } else {
                            Toast.makeText(
                                mContext,
                                "faild",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    } else {
                        Toast.makeText(mContext, "faild", Toast.LENGTH_SHORT)
                            .show()
                    }

                }


            })
    }
    private fun labDeleteVacation(vacationId:Int) {

        apiClient = ApiClient()
        sessionManager = SessionManager(mContext)
        var url=Q.LAB_DELETE_VACATION_API+"/$vacationId"
        apiClient.getApiService(mContext).deleteVacation(url)
            .enqueue(object : Callback<BaseResponce<Any>> {
                override fun onFailure(
                    call: Call<BaseResponce<Any>>,
                    t: Throwable
                ) {

                }

                override fun onResponse(
                    call: Call<BaseResponce<Any>>,
                    response: Response<BaseResponce<Any>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            labVacationDatesObserver()
                        } else {
                            Toast.makeText(
                                mContext,
                                "faild",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    } else {
                        Toast.makeText(mContext, "faild", Toast.LENGTH_SHORT)
                            .show()
                    }

                }


            })
    }

    private fun vacationDatesObserver() {
        list.clear()
        notifyDataSetChanged()
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).doctorVacanciesDates()
            .enqueue(object : Callback<BaseResponce<List<VacancyModel>>> {
                override fun onFailure(
                    call: Call<BaseResponce<List<VacancyModel>>>,
                    t: Throwable
                ) {

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
                                        list.add(date)
                                    }
                                    notifyDataSetChanged()

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
    private fun labVacationDatesObserver() {
        list.clear()
        notifyDataSetChanged()
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).labVacanciesDates()
            .enqueue(object : Callback<BaseResponce<List<VacancyModel>>> {
                override fun onFailure(
                    call: Call<BaseResponce<List<VacancyModel>>>,
                    t: Throwable
                ) {

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
                                        list.add(date)
                                    }
                                    notifyDataSetChanged()

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



}