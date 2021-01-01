package com.seha_khanah_doctors.modules.home.schedual

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.modules.home.schedual.addReservation.RservationDatesActivity
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.remote.objects.doctor.ReservationModel
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.fragment_appointments.*
import kotlinx.android.synthetic.main.no_search_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AppointmentsFragment : Fragment() {
    var currentDate=""
    var currentyear=0
    var currentmonth=0
    var currentday=0
    val calendar= Calendar.getInstance()
    val yearformat = SimpleDateFormat("yyyy", Locale.ENGLISH)
    val allformat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
    val monthformat = SimpleDateFormat("MM", Locale.ENGLISH)
    val dayformat = SimpleDateFormat("dd", Locale.ENGLISH)
    val year=yearformat.format(calendar.get(Calendar.YEAR)).toInt()
    val month=monthformat.format(calendar.get(Calendar.MONTH)).toInt()
    val day=dayformat.format(calendar.get(Calendar.DAY_OF_MONTH)).toInt()
    private var allReservationsList: MutableList<ReservationModel> = ArrayList()
    private var filteredReservationsList: MutableList<ReservationModel> = ArrayList()
    lateinit var dpd: DatePickerDialog



    //private var allRecervationsAddapter: AllRecervationsAdapter? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(mContext!!.preferences!!.getString(Q.USER_TYPE,"")){
            Q.USER_DOCTOR->{
                appointmentsObserver()
                onAddNewResrvationClicked()
            }
            Q.USER_LAB->{
                labAppointmentsObserver()
                onAddNewResrvationClicked()
            }
            Q.USER_PHARM->{}

        }

        calenderHandeler()
        initRVAdapter()
        pickDate()
    }

    private fun appointmentsObserver() {
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        onObserveStart()
        apiClient.getApiService(mContext!!).fitchAllReservationsList()
            .enqueue(object : Callback<BaseResponce<List<ReservationModel>>> {
                override fun onFailure(call: Call<BaseResponce<List<ReservationModel>>>, t: Throwable) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<List<ReservationModel>>>,
                    response: Response<BaseResponce<List<ReservationModel>>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                if (it.isNotEmpty()) {
                                    onObserveSuccess()
                                    allReservationsList.addAll(it)
                                    var cd="$currentyear-$currentmonth-$currentday"
                                    it.forEach { reservation:ReservationModel->
                                        if(reservation.booking_date.contains(cd)){
                                            reservation.date=reservation.booking_date.split(" ")[0]
                                            reservation.time=reservation.booking_date.split(" ")[1]
                                            filteredReservationsList.add(reservation)
                                        }

                                    }
                                    if(filteredReservationsList.isEmpty()){
                                        onObservefaled()
                                    }else{
                                        onObserveSuccess()
                                    }

                                } else {
                                    onObservefaled()
                                }

                            }
                        } else {
                            onObservefaled()
                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }
    private fun labAppointmentsObserver() {
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        onObserveStart()
        apiClient.getApiService(mContext!!).fitchAllLabReservationsList()
            .enqueue(object : Callback<BaseResponce<List<ReservationModel>>> {
                override fun onFailure(call: Call<BaseResponce<List<ReservationModel>>>, t: Throwable) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<List<ReservationModel>>>,
                    response: Response<BaseResponce<List<ReservationModel>>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                if (it.isNotEmpty()) {
                                    onObserveSuccess()
                                    allReservationsList.addAll(it)
                                    var cd="$currentyear-$currentmonth-$currentday"
                                    it.forEach { reservation:ReservationModel->
                                        if(reservation.booking_date.contains(cd)){
                                            reservation.date=reservation.booking_date.split(" ")[0]
                                            reservation.time=reservation.booking_date.split(" ")[1]
                                            filteredReservationsList.add(reservation)
                                        }

                                    }
                                    if(filteredReservationsList.isEmpty()){
                                        onObservefaled()
                                    }else{
                                        onObserveSuccess()
                                    }

                                } else {
                                    onObservefaled()
                                }

                            }
                        } else {
                            onObservefaled()
                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }

    private fun initRVAdapter() {
        val layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        all_days_rv.layoutManager = layoutManager
        //allRecervationsAddapter = AllRecervationsAdapter(mContext!!,filteredReservationsList)
       // all_days_rv.adapter = allRecervationsAddapter
    }

    private fun onObserveStart() {
        progrss_lay?.visibility = View.VISIBLE
        all_days_rv?.visibility = View.GONE
        no_search_lay?.visibility = View.GONE
    }

    private fun onObserveSuccess() {
        progrss_lay.visibility = View.GONE
        all_days_rv.visibility = View.VISIBLE
        no_search_lay.visibility = View.GONE
    }

    private fun onObservefaled() {
        progrss_lay.let {
            it.visibility = View.GONE
            all_days_rv.visibility = View.GONE
            no_search_lay.visibility = View.VISIBLE
            no_data_txt.text=getString(R.string.no_appointments)
            no_data_img.setImageResource(R.drawable.calendar_ic)
        }

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

    var mContext: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as MainActivity

    }
    private fun calenderHandeler(){



        currentDate = allformat.format(Date()).toString()
        currentyear = yearformat.format(Date()).toInt()
        currentmonth = monthformat.format(Date()).toInt()
        currentday = dayformat.format(Date()).toInt()
        var dayName=""
        when (calendar.get(Calendar.DAY_OF_WEEK)){
            1->{dayName=mContext!!.getString(R.string.sun)}
            2->{dayName=mContext!!.getString(R.string.mon)}
            3->{dayName=mContext!!.getString(R.string.tus)}
            4->{dayName=mContext!!.getString(R.string.wed)}
            5->{dayName=mContext!!.getString(R.string.thu)}
            6->{dayName=mContext!!.getString(R.string.fri)}
            7->{dayName=mContext!!.getString(R.string.sat)}
        }
        dates_page_date_txt.text="$dayName ${currentDate.split(" ")[0]}"

    }
    private fun pickDate(){
        var calendar= Calendar.getInstance()

        var year=calendar.get(Calendar.YEAR)
        var month=calendar.get(Calendar.MONTH)
        var day=calendar.get(Calendar.DAY_OF_MONTH)
        date_picker_btn.setOnClickListener {
             dpd= DatePickerDialog(mContext!!,
                { view, myear, mMonth, mdayOfMonth ->
                    filteredReservationsList.clear()

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
                    var selectedDate = "$myear-$month-$day"
                    val selectedate = SimpleDateFormat("yyyy-MM-dd").parse(selectedDate)
                    var dayname=SimpleDateFormat("EEEE").format(selectedate)
                    dates_page_date_txt.text="$dayname ${selectedDate}"
                    allReservationsList.forEach {
                        if (it.booking_date.contains(selectedDate)) {
                            it.date = it.booking_date.split(" ")[0]
                            it.time = it.booking_date.split(" ")[1]
                            filteredReservationsList.add(it)
                        }
                    }
                    if(filteredReservationsList.isEmpty()){
                        onObservefaled()
                    }else{
                        onObserveSuccess()
                    }
                   // allRecervationsAddapter!!.notifyDataSetChanged()


                }, year, month, day
            )
            dpd.show()

        }
    }
    private fun onAddNewResrvationClicked(){
        add_date_btn.setOnClickListener{
            val intent = Intent(mContext, RservationDatesActivity::class.java)
            intent.putExtra("doc_id",mContext!!.preferences!!.getInteger(Q.USER_ID,0))
            mContext!!.startActivity(intent)

        }
    }

}

