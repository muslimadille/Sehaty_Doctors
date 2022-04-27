package com.muslim_adel.sehatydoctors.modules.home.schedual

import android.app.TimePickerDialog
import android.content.Context
import android.os.Handler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.sehatydoctors.remote.objects.AllTimeModel
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.modules.home.schedual.EditWorkingDaysActivity
import com.seha_khanah_doctors.remote.objects.doctor.DurationModel
import com.seha_khanah_doctors.remote.objects.doctor.WorkingDatesModel
import com.seha_khanah_doctors.remote.objects.doctor.workingTimeModel
import com.seha_khanah_doctors.utiles.ComplexPreferences
import com.seha_khanah_doctors.utiles.Q
import com.seha_khanah_doctors.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.activity_edit_working_days.*
import kotlinx.android.synthetic.main.edit_work_days_item.view.*
import kotlinx.android.synthetic.main.fragment_registration3.*
import kotlinx.android.synthetic.main.working_time_item.view.*
import java.util.*


class EditWorkDaysAdapter(
    private var mContext: EditWorkingDaysActivity,
    private val list: MutableList<WorkingDatesModel>,
    private val allTimesList: MutableList<workingTimeModel>,
    private val allDurationsList: MutableList<DurationModel>


) : RecyclerView.Adapter<EditWorkDaysAdapter.ViewHolder>() {
    var preferences: ComplexPreferences? = null
    init {

        preferences = ComplexPreferences.getComplexPreferences(mContext as EditWorkingDaysActivity, Q.PREF_FILE, Q.MODE_PRIVATE)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(R.layout.edit_work_days_item, parent, false)
        return ViewHolder(convertView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = list[position]
        lateinit var startTimeSpinnerAdapter: SpinnerAdapterCustomFont
        lateinit var endTimeSpinnerAdapter: SpinnerAdapterCustomFont
        lateinit var durationTimeSpinnerAdapter: SpinnerAdapterCustomFont
        var startNameList = ArrayList<String>()
        var endNameList = ArrayList<String>()
        var durationNameList = ArrayList<String>()

        var startTimeIndex=allTimesList.indexOf(list[position].time_from)
        var endTimeIndex=allTimesList.indexOf(list[position].time_to)
        var durationIndex=allDurationsList.indexOf(list[position].duration)



        //handle switch=============================================================================
        if (preferences!!.getString("language","")=="Arabic"){
            holder.day_name_txt.let {  it!!.text=date.day.name_ar}
        }else{
            holder.day_name_txt.let {  it!!.text=date.day.name_ar}
        }

        holder.state_sw!!.isChecked = date.status==1
        if(holder.state_sw.isChecked){
            holder.detals_lay!!.visibility=View.VISIBLE
        }else{
            holder.detals_lay!!.visibility=View.GONE
        }
        holder.state_sw.setOnClickListener {
            if(holder.state_sw.isChecked){
                mContext.workingHoursList[position].status=1
                holder.detals_lay!!.visibility=View.VISIBLE
            }else{
                mContext.workingHoursList[position].status=0
                holder.detals_lay!!.visibility=View.GONE
            }
        }
        //init data ==================================================================================

        for(i in 0 until allTimesList.size){
            if (preferences!!.getString("language","")=="Arabic"){
                startNameList.add(allTimesList[i].time_ar)
                endNameList.add(allTimesList[i].time_ar)

            }else{
                startNameList.add(allTimesList[i].time_en)
                endNameList.add(allTimesList[i].time_en)

            }
        }
        for(i in 0 until allDurationsList.size){
            if (preferences!!.getString("language","")=="Arabic"){
                durationNameList.add(allDurationsList[i].duration_ar)

            }else{
                durationNameList.add(allDurationsList[i].duration_en)

            }
        }

        startTimeSpinnerAdapter= SpinnerAdapterCustomFont(mContext!!, android.R.layout.simple_spinner_item, startNameList)
        startTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        startTimeSpinnerAdapter.textSize = 12
        holder.start_time_spinner!!.adapter = startTimeSpinnerAdapter
        holder.start_time_spinner.setSelection(startTimeIndex)
        holder.start_time_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, spinnerPosition: Int, id: Long) {
                if(true){
                    mContext.workingHoursList[holder.getAdapterPosition()].time_from_id=allTimesList[spinnerPosition].id
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        endTimeSpinnerAdapter= SpinnerAdapterCustomFont(mContext!!, android.R.layout.simple_spinner_item, endNameList)
        endTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        endTimeSpinnerAdapter.textSize = 12
        holder.end_time_spinner!!.adapter = endTimeSpinnerAdapter
        holder.end_time_spinner.setSelection(endTimeIndex)
        holder.end_time_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, spinnerPosition: Int, id: Long) {
                if(true){
                    mContext.workingHoursList[holder.getAdapterPosition()].time_to_id=allTimesList[spinnerPosition].id
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        durationTimeSpinnerAdapter= SpinnerAdapterCustomFont(mContext!!, android.R.layout.simple_spinner_item, durationNameList)
        durationTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        durationTimeSpinnerAdapter.textSize = 12
        holder.waiting_time_spinner!!.adapter = durationTimeSpinnerAdapter
        holder.waiting_time_spinner.setSelection(durationIndex)
        holder.waiting_time_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, spinnerPosition: Int, id: Long) {
                if(allDurationsList.isNotEmpty()&&mContext.workingHoursList.isNotEmpty()){
                    try {
                        Handler().postDelayed({
                            mContext.workingHoursList[holder.getAdapterPosition()].duration_id=allDurationsList[spinnerPosition].id
                        }, 20)

                    }catch (e:Error){

                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        //set new data==============================================================================

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val day_name_txt: TextView? =view.day_name_txt
        val start_time_spinner: Spinner? =view.start_time_spinner
        val end_time_spinner: Spinner? =view.end_time_spinner
        val waiting_time_spinner: Spinner? =view.waiting_time_spinner
        val state_sw:Switch?=view.state_sw
        val detals_lay:CardView?=view.detals_lay


    }



}