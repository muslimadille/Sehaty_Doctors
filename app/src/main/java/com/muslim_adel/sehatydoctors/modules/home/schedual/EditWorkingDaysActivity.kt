package com.seha_khanah_doctors.modules.home.schedual

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.remote.objects.OffersSubGategory
import com.seha_khanah_doctors.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.activity_add_dpctor_offer.*
import kotlinx.android.synthetic.main.activity_edit_working_days.*
import java.util.*

class EditWorkingDaysActivity : BaseActivity() {
    private var satselectedWairingTimeIndex=0
    private lateinit var satwatingTimeSpinnerAdapter: SpinnerAdapterCustomFont
    private var satwaitingTimeList = ArrayList<String>()

    private var sunselectedWairingTimeIndex=0
    private lateinit var sunwatingTimeSpinnerAdapter: SpinnerAdapterCustomFont
    private var sunwaitingTimeList = ArrayList<String>()

    private var monselectedWairingTimeIndex=0
    private lateinit var monwatingTimeSpinnerAdapter: SpinnerAdapterCustomFont
    private var monwaitingTimeList = ArrayList<String>()

    private var tusselectedWairingTimeIndex=0
    private lateinit var tuswatingTimeSpinnerAdapter: SpinnerAdapterCustomFont
    private var tuswaitingTimeList = ArrayList<String>()

    private var wedselectedWairingTimeIndex=0
    private lateinit var wedwatingTimeSpinnerAdapter: SpinnerAdapterCustomFont
    private var wedwaitingTimeList = ArrayList<String>()

    private var thuselectedWairingTimeIndex=0
    private lateinit var thuwatingTimeSpinnerAdapter: SpinnerAdapterCustomFont
    private var thuwaitingTimeList = ArrayList<String>()

    private var friselectedWairingTimeIndex=0
    private lateinit var friwatingTimeSpinnerAdapter: SpinnerAdapterCustomFont
    private var friwaitingTimeList = ArrayList<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_working_days)
        switchsHandeler()
        pickTime()
        fillSpinners()

    }
    private  fun switchsHandeler(){
        sat_sw.setOnClickListener {
            if(sat_sw.isChecked){
                sat_detals_lay.visibility=View.VISIBLE
            }else{
                sat_detals_lay.visibility=View.GONE

            }
        }
        //----------------------------------------------
        sun_sw.setOnClickListener {
            if(sun_sw.isChecked){
                sun_detals_lay.visibility=View.VISIBLE
            }else{
                sun_detals_lay.visibility=View.GONE

            }
        }
        //--------------------------------------------
        mon_sw.setOnClickListener {
            if(mon_sw.isChecked){
                mon_detals_lay.visibility=View.VISIBLE
            }else{
                mon_detals_lay.visibility=View.GONE

            }
        }
        //----------------------------------------------
        tus_sw.setOnClickListener {
            if(tus_sw.isChecked){
                tus_detals_lay.visibility=View.VISIBLE
            }else{
                tus_detals_lay.visibility=View.GONE

            }
        }
        //----------------------------------------------
        wed_sw.setOnClickListener {
            if(wed_sw.isChecked){
                wed_detals_lay.visibility=View.VISIBLE
            }else{
                wed_detals_lay.visibility=View.GONE

            }
        }
        //----------------------------------------------
        thu_sw.setOnClickListener {
            if(thu_sw.isChecked){
                thu_detals_lay.visibility=View.VISIBLE
            }else{
                thu_detals_lay.visibility=View.GONE

            }
        }
        //----------------------------------------------
        fri_sw.setOnClickListener {
            if(fri_sw.isChecked){
                fri_detals_lay.visibility=View.VISIBLE
            }else{
                fri_detals_lay.visibility=View.GONE
            }
        }
    }
    private fun pickTime(){
        sat_start_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)

            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    sat_start_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }

        sat_end_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)


            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    sat_end_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }
        //---------------------------------------------------------------------------------
        sun_start_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)

            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    sun_start_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }

        sun_end_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)


            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    sun_end_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }
        //------------------------------------------------------------------------------------------------
        mon_start_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)

            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    mon_start_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }

        mon_end_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)


            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    mon_end_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }
        //------------------------------------------------------------------------------------------------
        tus_start_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)

            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    tus_start_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }

        tus_end_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)


            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    tus_end_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }
        //------------------------------------------------------------------------------------------------
        wed_start_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)

            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    wed_start_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }

        wed_end_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)


            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    wed_end_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }
        //------------------------------------------------------------------------------------------------
        thu_start_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)

            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    thu_start_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }

        thu_end_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)


            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    thu_end_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }
        //------------------------------------------------------------------------------------------------
        fri_start_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)

            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    fri_start_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }

        fri_end_time_txt.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val d=mcurrentTime.get(Calendar.AM_PM)


            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    fri_end_time_txt.setText(String.format("%d : %d %d", hourOfDay, minute,d))
                }
            }, hour, minute, true)
            mTimePicker.show()
        }
    }
    private  fun fillSpinners(){
        satwaitingTimeList.add(getString(R.string.waiting_time))
        satwaitingTimeList.add("5"+getString(R.string.min))
        satwaitingTimeList.add("10"+getString(R.string.min))
        satwaitingTimeList.add("15"+getString(R.string.min))
        satwaitingTimeList.add("20"+getString(R.string.min))
        satwaitingTimeList.add("25"+getString(R.string.min))
        satwaitingTimeList.add("30"+getString(R.string.min))
        satwaitingTimeList.add("35"+getString(R.string.min))
        satwaitingTimeList.add("40"+getString(R.string.min))
        satwaitingTimeList.add("45"+getString(R.string.min))
        satwaitingTimeList.add("50"+getString(R.string.min))
        satwaitingTimeList.add("55"+getString(R.string.min))
        satwaitingTimeList.add("60"+getString(R.string.min))

        satwatingTimeSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, satwaitingTimeList)
        satwatingTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        satwatingTimeSpinnerAdapter.textSize = 12
        sat_waiting_time_spinner.adapter = satwatingTimeSpinnerAdapter
        satwatingTimeSpinnerAdapter.notifyDataSetChanged()


        sunwatingTimeSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, satwaitingTimeList)
        sunwatingTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sunwatingTimeSpinnerAdapter.textSize = 12
        sun_waiting_time_spinner.adapter = sunwatingTimeSpinnerAdapter
        sunwatingTimeSpinnerAdapter.notifyDataSetChanged()


        monwatingTimeSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, satwaitingTimeList)
        monwatingTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monwatingTimeSpinnerAdapter.textSize = 12
        mon_waiting_time_spinner.adapter = monwatingTimeSpinnerAdapter
        monwatingTimeSpinnerAdapter.notifyDataSetChanged()

        tuswatingTimeSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, satwaitingTimeList)
        tuswatingTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tuswatingTimeSpinnerAdapter.textSize = 12
        tus_waiting_time_spinner.adapter = tuswatingTimeSpinnerAdapter
        tuswatingTimeSpinnerAdapter.notifyDataSetChanged()

        wedwatingTimeSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, satwaitingTimeList)
        wedwatingTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        wedwatingTimeSpinnerAdapter.textSize = 12
        wed_waiting_time_spinner.adapter = wedwatingTimeSpinnerAdapter
        wedwatingTimeSpinnerAdapter.notifyDataSetChanged()

        thuwatingTimeSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, satwaitingTimeList)
        thuwatingTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        thuwatingTimeSpinnerAdapter.textSize = 12
        thu_waiting_time_spinner.adapter = thuwatingTimeSpinnerAdapter
        thuwatingTimeSpinnerAdapter.notifyDataSetChanged()

        friwatingTimeSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, satwaitingTimeList)
        friwatingTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        friwatingTimeSpinnerAdapter.textSize = 12
        fri_waiting_time_spinner.adapter = thuwatingTimeSpinnerAdapter
        friwatingTimeSpinnerAdapter.notifyDataSetChanged()
    }
}