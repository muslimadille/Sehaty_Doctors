package com.muslim_adel.sehatydoctors.modules.home.schedual.addReservation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.remote.objects.Times
import kotlinx.android.synthetic.main.time_item.view.*

class ReservationTimesAdapter(
    private val mContext: ReservationTimesActivity,
    private val list: MutableList<Times>
) : RecyclerView.Adapter<ReservationTimesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(
            R.layout.time_item, parent, false
        )
        return ViewHolder(convertView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(true/*mContext.key!=2*/){
            val time = list[position]
            if(time.status=="1"){
                holder.time_txt.text = time.time
                holder.time_txt.setOnClickListener {
                    val intent = Intent(mContext, AddNewReservationActivity::class.java)
                    intent.putExtra("selected_date",mContext.selecteddate)
                     intent.putExtra("date_name",mContext.datename)
                     intent.putExtra("selected_time",time.time)
                    mContext.startActivity(intent)}
            }else return
        }else{
            val time = list[position]
            if(time.status=="1"){
                holder.time_txt.text = time.time
                holder.time_txt.setOnClickListener {
                    val intent = Intent(mContext, AddNewReservationActivity::class.java)
                   /* intent.putExtra("date_id",mContext.dateId)
                    intent.putExtra("lab_id",mContext.lab_id)
                    intent.putExtra("time_id",time.id)
                    intent.putExtra("time",time.time.toString())
                    intent.putExtra("service_id",mContext.service_id)*/
                    mContext.startActivity(intent)
                }
            }else return
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time_txt: TextView = view.time_item_txt
    }
}
    
