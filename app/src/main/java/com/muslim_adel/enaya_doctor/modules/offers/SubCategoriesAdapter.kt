package com.muslim_adel.enaya_doctor.modules.offers

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.remote.objects.OffersCategory
import com.muslim_adel.enaya_doctor.remote.objects.OffersSubGategory
import kotlinx.android.synthetic.main.offer_category_frist_item.view.*
import kotlinx.android.synthetic.main.offers_second_item.view.*
import kotlinx.android.synthetic.main.sub_category_item.view.*


class SubCategoriesAdapter(
    private val mContext: NewDoctorAddOfferActivity,
    private val list: MutableList<OffersSubGategory>


) : RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>() {
    var viewType=-1
    var lastPosition=0
    var r1p: MutableList<Int> = ArrayList()
    var r2p: MutableList<Int> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(R.layout.sub_category_item, parent, false)
        return ViewHolder(convertView)


    }

    override fun getItemCount(): Int {
        if(list.size!=0){
            if((list.size)%2>0){
                return ((list.size-1)/2)+1
            }else{
                return (list.size/2)+1
            }
        }
        else return list.size

    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subCategory = list[position]

        if (mContext.preferences!!.getString("language","")=="Arabic"){
            holder.sub_category_txt!!.text=subCategory.name_ar
        }else{
            holder.sub_category_txt!!.text=subCategory.name_en
        }
        if(mContext.selectedSubCtegory==position){
            holder.sub_category_txt.setTextColor(mContext.getColor(R.color.blue))
        }else{
            holder.sub_category_txt.setTextColor(mContext.getColor(R.color.gray))
        }
        holder.sub_category_txt!!.setOnClickListener {
            mContext.selectedSubCtegory=position
            mContext.refreshRecycler2()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sub_category_txt: TextView? =view.sub_category_txt

    }

}