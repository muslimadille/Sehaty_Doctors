package com.seha_khanah_doctors.modules.home.schedual.addReservation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.remote.objects.Date
import kotlinx.android.synthetic.main.resevation_date_item.view.*


class ReservationDatesAdapter(
    private val mContext: RservationDatesActivity,
    private val list: MutableList<Date>
) : RecyclerView.Adapter<ReservationDatesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(R.layout.resevation_date_item, parent, false)
        return ViewHolder(convertView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = list[position]
        if (date.status==1){
            if (mContext.preferences!!.getString("language","")=="Arabic"){
                   holder.res_item_day_name_txt.text=date.day_ar+"\n ${date.date}"
                if(date.times!=null){
                    if (date.times.isNotEmpty()){
                        holder.res_from_itme_date_txt.text=date.times[0].time
                        if (date.times.size>1){
                            holder.res_to_itme_date_txt.text=date.times[date.times.size-1].time
                        }else{
                            holder.res_to_itme_date_txt.text=""
                        }

                    }else{
                        holder.res_from_itme_date_txt.text="00:00"
                        holder.res_to_itme_date_txt.text="00:00"
                    }

                }else{
                    holder.res_from_itme_date_txt.text="00:00"
                    holder.res_to_itme_date_txt.text="00:00"
                }


            }else{
                holder.res_item_day_name_txt.text=date.day_en+"\n ${date.date}"
                if(date.times!=null){
                    if (date.times.isNotEmpty()){
                        holder.res_from_itme_date_txt.text=date.times[0].time
                        if (date.times.size>1){
                            holder.res_to_itme_date_txt.text=date.times[date.times.size-1].time
                        }else{
                            holder.res_to_itme_date_txt.text="00:00"
                        }

                    }else{
                        holder.res_from_itme_date_txt.text="00:00"
                        holder.res_to_itme_date_txt.text="00:00"
                    }

                }else{
                    holder.res_from_itme_date_txt.text="00:00"
                    holder.res_to_itme_date_txt.text="00:00"
                }
            }

            holder.date_btn.setOnClickListener {
                val intent = Intent(mContext, ReservationTimesActivity::class.java)
                intent.putExtra("date_id",date.id)
                mContext.startActivity(intent)
            }
        }else return

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val res_item_day_name_txt: TextView =view.res_item_day_name_txt
        val res_from_itme_date_txt: TextView =view.res_from_itme_date_txt
        val res_to_itme_date_txt: TextView =view.res_to_itme_date_txt
        val date_btn: LinearLayout =view.date_btn
    }
}