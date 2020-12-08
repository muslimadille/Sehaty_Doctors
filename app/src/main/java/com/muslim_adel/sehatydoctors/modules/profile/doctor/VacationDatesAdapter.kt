package com.muslim_adel.sehatydoctors.modules.profile.doctor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.home.MainActivity
import com.muslim_adel.sehatydoctors.remote.objects.doctor.VacancyModel
import com.muslim_adel.sehatydoctors.utiles.ComplexPreferences
import com.muslim_adel.sehatydoctors.utiles.Q
import kotlinx.android.synthetic.main.vacation_dates_item.view.*



class VacationDatesAdapter(
    private val mContext: Context,
    private val list: MutableList<VacancyModel>
) : RecyclerView.Adapter<VacationDatesAdapter.ViewHolder>() {
    var preferences: ComplexPreferences? = null
    init {
        preferences = ComplexPreferences.getComplexPreferences(mContext as MainActivity, Q.PREF_FILE, Q.MODE_PRIVATE)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(R.layout.vacation_dates_item, parent, false)
        return ViewHolder(convertView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vacation = list[position]

        holder.vacation_date_from_txt?.let { it.text=vacation.start_date?.let { it  }}
        holder.vacation_date_to_txt?.let { it .text=vacation.end_date?.let { it}}

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val vacation_date_from_txt: TextView? =view.vacation_date_from_txt
        val vacation_date_to_txt: TextView? =view.vacation_date_to_txt

    }


}