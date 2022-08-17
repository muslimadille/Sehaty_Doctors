package com.muslim_adel.enaya_doctor.modules.profile.labs.editLabServices

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.remote.objects.LaboratoryServices
import kotlinx.android.synthetic.main.reagon_item.view.*

class LabServicesAdapter(
    private val mContext: EditLabServicesActivity,
    private val list: MutableList<LaboratoryServices>,
    private val dateId: Int,
    private val labId: Long

) : RecyclerView.Adapter<LabServicesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(R.layout.reagon_item, parent, false)
        return ViewHolder(convertView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = list[position]
        if (mContext.preferences!!.getString("language","")=="Arabic"){
            holder.txtTitle.text = service.name_ar
        }else{
            holder.txtTitle.text = service.name_en
        }
        holder.reagonsLay.setOnClickListener {
        }
        holder.lab_service_delete_btn.setOnClickListener{
            list.removeAt(position)
            notifyDataSetChanged()
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitle: TextView = view.reagons_txt
        val reagonsLay: LinearLayout = view.reagons_lay
        val lab_service_delete_btn:ImageView=view.lab_service_delete_btn

    }
}
