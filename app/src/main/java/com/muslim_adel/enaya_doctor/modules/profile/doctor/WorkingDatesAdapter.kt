package com.muslim_adel.enaya_doctor.modules.profile.doctor

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.modules.offers.OfferDetailsActivity
import com.muslim_adel.enaya_doctor.remote.objects.Offer
import com.muslim_adel.enaya_doctor.remote.objects.doctor.WorkingDatesModel
import com.muslim_adel.enaya_doctor.utiles.ComplexPreferences
import com.muslim_adel.enaya_doctor.utiles.Q
import kotlinx.android.synthetic.main.offer_item.view.*
import kotlinx.android.synthetic.main.offer_item.view.offer_title_txt
import kotlinx.android.synthetic.main.working_time_item.view.*


class WorkingDatesAdapter(
    private val mContext: Context,
    private val list: MutableList<WorkingDatesModel>
) : RecyclerView.Adapter<WorkingDatesAdapter.ViewHolder>() {
    var preferences: ComplexPreferences? = null
    init {
        preferences = ComplexPreferences.getComplexPreferences(mContext as MainActivity, Q.PREF_FILE, Q.MODE_PRIVATE)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(R.layout.working_time_item, parent, false)
        return ViewHolder(convertView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = list[position]

       if(date.status==1){
           if (preferences!!.getString("language","")=="Arabic"){
               holder.working_date_name?.let { it.text=date.day?.let { it.name_ar  }}
               holder.from_date_txt?.let { it .text=date.time_from?.let { it.time_ar }}
               holder.to_date_txt?.let { it.text=date.time_to?.let { it.time_ar } }

           }else{

               holder.working_date_name?.let { it.text=date.day.name_en }
               holder.from_date_txt?.let { it .text=date.time_from.time_en}
               holder.to_date_txt?.let { it.text=date.time_to.time_en }
           }
       }




    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val working_date_name: TextView? =view.working_date_name
        val from_date_txt: TextView? =view.from_date_txt
        val to_date_txt: TextView? =view.to_date_txt

    }


}