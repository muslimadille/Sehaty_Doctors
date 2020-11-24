package com.muslim_adel.sehatydoctors.modules.home.schedual

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.remote.objects.Date
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
                holder.res_from_itme_date_txt.text=date.times[0].time
                holder.res_to_itme_date_txt.text=date.times[date.times.size-1].time
            }else{
                holder.res_item_day_name_txt.text=date.day_en+"\n ${date.date}"
                holder.res_from_itme_date_txt.text=date.times[0].time
                holder.res_to_itme_date_txt.text=date.times[date.times.size-1].time
            }

            holder.date_btn.setOnClickListener {
                /*val intent = Intent(mContext, DatesActivity::class.java)
                intent.putExtra("date_id",date.id)
                intent.putExtra("firstName_ar",mContext.firstName_ar)
                intent.putExtra("firstName_en",mContext.firstName_en)
                intent.putExtra("lastName_ar",mContext.lastName_ar)
                intent.putExtra("lastName_en",mContext.lastName_en)
                intent.putExtra("featured",mContext.featured)
                intent.putExtra("doctor_id",mContext.id)
                intent.putExtra("phonenumber",mContext.phonenumber)
                intent.putExtra("price",mContext.price)
                intent.putExtra("profissionalTitle_ar",mContext.profissionalTitle_ar)
                intent.putExtra("profissionalTitle_en",mContext.profissionalTitle_en)
                intent.putExtra("streetName_ar",mContext.streetName_ar)
                intent.putExtra("streetName_en",mContext.streetName_en)
                intent.putExtra("apartmentNum_ar",mContext.apartmentNum_ar)
                intent.putExtra("apartmentNum_en",mContext.apartmentNum_en)
                intent.putExtra("landmark_ar",mContext.landmark_ar)
                intent.putExtra("landmark_en",mContext.landmark_en)
                intent.putExtra("buildingNum_ar",mContext.buildingNum_ar)
                intent.putExtra("role",mContext.role)
                intent.putExtra("buildingNum_en",mContext.buildingNum_en)
                mContext.startActivity(intent)*/
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