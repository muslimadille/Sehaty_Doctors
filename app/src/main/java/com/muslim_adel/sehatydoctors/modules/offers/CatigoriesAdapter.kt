package com.muslim_adel.sehatydoctors.modules.offers

import android.content.Context
import android.content.Intent
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
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.remote.objects.OffersCategory
import kotlinx.android.synthetic.main.offer_category_frist_item.view.*
import kotlinx.android.synthetic.main.offers_second_item.view.*

class CatigoriesAdapter(
    private val mContext: NewDoctorAddOfferActivity,
    private val list: MutableList<OffersCategory>

) : RecyclerView.Adapter<CatigoriesAdapter.ViewHolder>() {
    var viewType=-1
    var lastPosition=0
    var r1p: MutableList<Int> = ArrayList()
    var r2p: MutableList<Int> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (viewType == 1) {
            val convertView = inflater.inflate(R.layout.offer_category_frist_item, parent, false)
            return ViewHolder(convertView)
        } else {
            val convertView = inflater.inflate(R.layout.offers_second_item, parent, false)
            return ViewHolder(convertView)        }


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
        val category = list[position]

        if (viewType==1){
            if(mContext.selectedCtegory==position){
                holder.category_item_colored_lay!!.setBackgroundColor(mContext.getColor(R.color.blue))
            }else{
                holder.category_item_colored_lay!!.setBackgroundColor(mContext.getColor(R.color.white))
            }
            holder.category_img!!.setOnClickListener {
                mContext.selectedCtegory=position
                mContext.refreshRecycler()
            }
            Glide.with(mContext).applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.person_ic)
                    .error(R.drawable.person_ic))
                .load(category.efeaturedl)
                .centerCrop()
                .into(holder.category_img!!)
            if (mContext.preferences!!.getString("language","")=="Arabic"){
                holder.category_title_txt?.text=category.name_ar

            }else{
                holder.category_title_txt?.text=category.name_en

            }
        }
        else if (viewType==2){
            if(position==1){
                lastPosition=position
                r1p.add(lastPosition)
            }
            else{
                lastPosition=lastPosition+1
                r1p.add(lastPosition)
            }
            if (lastPosition<=list.size-1){
                if(mContext.selectedCtegory==r1p[position-1]){
                    holder.category_item1_colored_lay!!.setBackgroundColor(mContext.getColor(R.color.blue))
                }else{
                    holder.category_item1_colored_lay!!.setBackgroundColor(mContext.getColor(R.color.white))
                }
                holder.category1_img!!.setOnClickListener {
                    mContext.selectedCtegory=r1p[position-1]
                    mContext.refreshRecycler()

                }
                Glide.with(mContext).applyDefaultRequestOptions(
                    RequestOptions()
                        .placeholder(R.drawable.person_ic)
                        .error(R.drawable.person_ic))
                    .load(list[lastPosition].efeaturedl)
                    .centerCrop()
                    .into(holder.category1_img!!)
                if (mContext.preferences!!.getString("language","")=="Arabic"){
                    holder.category1_title_txt?.text=list[lastPosition].name_ar

                }else{
                    holder.category1_title_txt?.text=list[lastPosition].name_en

                }

                lastPosition=lastPosition+1
                if(lastPosition<=list.size-1){
                    r2p.add(lastPosition)

                    if(mContext.selectedCtegory==r2p[position-1]){
                        holder.category_item2_colored_lay!!.setBackgroundColor(mContext.getColor(R.color.blue))
                    }else{
                        holder.category_item2_colored_lay!!.setBackgroundColor(mContext.getColor(R.color.white))
                    }
                    holder.category2_img!!.setOnClickListener {
                        mContext.selectedCtegory=r2p[position-1]
                        mContext.refreshRecycler()

                    }
                    Glide.with(mContext).applyDefaultRequestOptions(
                        RequestOptions()
                            .placeholder(R.drawable.person_ic)
                            .error(R.drawable.person_ic))
                        .load(list[lastPosition].efeaturedl)
                        .centerCrop()
                        .into(holder.category2_img!!)
                    if (mContext.preferences!!.getString("language","")=="Arabic"){
                        holder.category2_title_txt?.text=list[lastPosition].name_ar
                    }else{
                        holder.category2_title_txt?.text=list[lastPosition].name_en
                    }
                }

            }



        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category_img: ImageView? =view.category_img
        val category_title_txt: TextView? =view.category_title_txt

        val category1_img: ImageView? =view.category1_img
        val category1_title_txt: TextView? =view.category1_title_txt

        val category2_img: ImageView? =view.category2_img
        val category2_title_txt: TextView? =view.category2_title_txt
        val category_item_colored_lay:LinearLayout?=view.category_item_colored_lay
        val category_item1_colored_lay:LinearLayout?=view.category_item1_colored_lay
        val category_item2_colored_lay:LinearLayout?=view.category_item2_colored_lay

    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            viewType=1
            return 1
        };
        else {
            viewType=2
            return 2
        }
    }
}