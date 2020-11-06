package com.muslim_adel.sehatydoctors.modules.base

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.muslim_adel.sehatydoctors.R

object GlideObject {

    fun GlideProfilePic(context: Context, imageName:String, imageView: ImageView){
        Glide.with(context).applyDefaultRequestOptions(
            RequestOptions()
            .placeholder(R.drawable.person_ic)
            .error(R.drawable.person_ic))
            .load(imageName)
            .fitCenter()
            .into(imageView)
    }
}