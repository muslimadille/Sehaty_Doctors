package com.muslim_adel.enaya_doctor.modules.home.schedual

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.remote.objects.doctor.DayItemModel
import com.muslim_adel.enaya_doctor.remote.objects.doctor.ReservationModel
import kotlinx.android.synthetic.main.all_days_item.view.*
import kotlinx.android.synthetic.main.appointment_list_item.view.*
import kotlinx.android.synthetic.main.appointment_list_item.view.appointment_item_patient_name

class AllDaysAdapter(
    private val mContext: MainActivity,
    private val list: MutableList<DayItemModel>) : RecyclerView.Adapter<AllDaysAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(R.layout.all_days_item, parent, false)
        return ViewHolder(convertView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reservation = list[position]


        holder.day_name_txt.text=reservation.dayName
        holder.moth_day_txt.text=reservation.dayOfMonth

        /*holder.disable_btn.setOnClickListener {
            holder.disable_btn.setImageResource(R.drawable.airplane)
        }*/
        holder.day_item_lay.setOnClickListener {
          mContext.intent= Intent(mContext,AllReservationsActivity::class.java)
            mContext.intent.putExtra("date",reservation.date)
            mContext.startActivity(mContext.intent)
        }




    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val day_name_txt: TextView = view.day_name_txt
        val moth_day_txt: TextView = view.moth_day_txt
        //val disable_btn: ImageView = view.disable_btn
        val day_item_lay: LinearLayout =view.day_item_lay
    }
}