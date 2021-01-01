package com.seha_khanah_doctors.modules.home.schedual

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.remote.objects.doctor.DayItemModel
import com.seha_khanah_doctors.remote.objects.doctor.WorkingDatesModel
import kotlinx.android.synthetic.main.fragment_all_days.*
import kotlinx.android.synthetic.main.fragment_appointments.*
import kotlinx.android.synthetic.main.fragment_appointments.all_days_rv
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AllDaysFragment : Fragment() {
    private var DaysList: MutableList<DayItemModel> = ArrayList()
    private var allDaysAddapter: AllDaysAdapter? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generatDaysList()
        initRVAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_days, container, false)
    }

private fun generatDaysList(){
    val allformat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
for(i in 0..20){
    var dayItemModel= DayItemModel("","","")

    val date = Date()
    val cal = Calendar.getInstance()
    cal.time = date
    cal.add(Calendar.DAY_OF_MONTH, i)
    val datePlusOneDay: Date = cal.time
    dayItemModel!!.date=allformat.format(datePlusOneDay).toString()+"-${getNextDays(i).get(Calendar.DAY_OF_WEEK)}"
    dayItemModel!!.dayOfMonth=getNextDays(i).time.toString().split(" ")[1]+" "+getNextDays(i).time.toString().split(" ")[2]
    var dayName=""
    when (getNextDays(i).get(Calendar.DAY_OF_WEEK)){
        1->{dayName=getString(R.string.sun)}
        2->{dayName=getString(R.string.mon)}
        3->{dayName=getString(R.string.tus)}
        4->{dayName=getString(R.string.wed)}
        5->{dayName=getString(R.string.thu)}
        6->{dayName=getString(R.string.fri)}
        7->{dayName=getString(R.string.sat)}
    }
    dayItemModel!!.dayName=dayName
    DaysList.add(dayItemModel)



}
}
    fun getNextDays(count:Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, +count)
        return calendar
    }
    private fun initRVAdapter() {
        val layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        all_days_list.layoutManager = layoutManager
        allDaysAddapter = AllDaysAdapter(mContext!!,DaysList)
        all_days_list.adapter = allDaysAddapter
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
}