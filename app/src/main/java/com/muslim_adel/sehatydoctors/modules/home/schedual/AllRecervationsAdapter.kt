package com.seha_khanah_doctors.modules.home.schedual

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.remote.objects.doctor.ReservationModel
import kotlinx.android.synthetic.main.appointment_list_item.view.*


class AllRecervationsAdapter(
    private val mContext: AllReservationsActivity,
    private val list: MutableList<ReservationModel>) : RecyclerView.Adapter<AllRecervationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(R.layout.appointment_list_item, parent, false)
        return ViewHolder(convertView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reservation = list[position]


            holder.appointment_item_patient_name.text=reservation.name
            if(reservation.time.split(":")[0].toInt()>12){
                holder.appointment_item_patient_time.text="${(reservation.time.split(":")[0].toInt())-12}:${reservation.time.split(":")[1]} ${mContext.getString(R.string.pm)}"
            }else{
                holder.appointment_item_patient_time.text="${(reservation.time.split(":")[0].toInt())}:${reservation.time.split(":")[1]} ${mContext.getString(R.string.am)}"
            }
        if(reservation.status_id==3){
            holder.go_to_clinic_btn.setBackgroundColor(mContext.getColor(R.color.green_light))
        }
        if(reservation.status_id==2){
            holder.didnot_come_btn.setBackgroundColor(mContext.getColor(R.color.red))
        }
        holder.go_to_clinic_btn.setOnClickListener {
            if(reservation.status_id!=2){
                mContext.changeReservationState(1,reservation.id)
                holder.go_to_clinic_btn.setBackgroundColor(mContext.getColor(R.color.green_light))
            }

        }
        holder.didnot_come_btn.setOnClickListener {
            if(reservation.status_id!=3){
                mContext.changeReservationState(2,reservation.id)
                holder.didnot_come_btn.setBackgroundColor(mContext.getColor(R.color.red))
            }

        }

        holder.reservation_item_lay.setOnClickListener {

        }

        /* holder.book_btn.setOnClickListener {
             val intent = Intent(mContext, DatesActivity::class.java)
             intent.putExtra("date_id", date.id)
             intent.putExtra("firstName_ar", mContext.firstName_ar)
             intent.putExtra("firstName_en", mContext.firstName_en)
             intent.putExtra("lastName_ar", mContext.lastName_ar)
             intent.putExtra("lastName_en", mContext.lastName_en)
             intent.putExtra("featured", mContext.featured)
             intent.putExtra("doctor_id", mContext.id)
             intent.putExtra("phonenumber", mContext.phonenumber)
             intent.putExtra("price", mContext.price)
             intent.putExtra("profissionalTitle_ar", mContext.profissionalTitle_ar)
             intent.putExtra("profissionalTitle_en", mContext.profissionalTitle_en)
             intent.putExtra("address_ar", mContext.address_ar)
             intent.putExtra("address_en", mContext.address_en)
             mContext.startActivity(intent)
         }*/


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appointment_item_patient_name: TextView = view.appointment_item_patient_name
        val appointment_item_patient_time: TextView = view.appointment_item_patient_time
        val reservation_item_lay: CardView = view.reservation_item_lay
        val go_to_clinic_btn: LinearLayout = view.go_to_clinic_btn
        val didnot_come_btn: LinearLayout = view.didnot_come_btn


    }
}