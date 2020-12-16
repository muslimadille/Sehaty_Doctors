package com.seha_khanah_doctors.utiles

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.seha_khanah_doctors.R

class SpinnerAdapterCustomFont (context: Context, resource: Int, internal var list: ArrayList<String>) : ArrayAdapter<String>(context, resource, list) {
    // Initialise custom font, for example:
    internal var font = ResourcesCompat.getFont(getContext(), R.font.google)

    var textSize = 0
    var positionToGray = 0
    var colors = intArrayOf(R.color.gray, R.color.white)



    // Affects default (closed) state of the spinner
    @SuppressWarnings("deprecation")
    override fun getView(i: Int, convertView: View?, parent: ViewGroup): View {
        var position = i
        if(position >= list.size) position = list.size-1

        val view = super.getView(position, convertView, parent) as TextView
        //view.typeface = font
        if (positionToGray == -1) {
            view.setTextColor(context.resources.getColor(colors[1]))
        } else {
            view.setTextColor(context.resources.getColor(colors[0]))

//            if (position == positionToGray) {
//                view.setTextColor(context.resources.getColor(colors[0]))
//            } else {
//                view.setTextColor(context.resources.getColor(colors[1]))
//            }
        }

        if(textSize != 0){
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        }

        view.text = list[position]

        return view
    }

    // Affects opened state of the spinner
    @SuppressWarnings("deprecation")
    override fun getDropDownView(i: Int, convertView: View?, parent: ViewGroup): View? {
        var position = i
        if(position >= list.size) position = list.size-1

        val view = super.getDropDownView(position, convertView, parent) as TextView
        //view.typeface = font
        if (positionToGray == -1) {
            view.setTextColor(context.resources.getColor(colors[1]))
        } else {
            view.setTextColor(context.resources.getColor(colors[0]))

//            if (position == positionToGray) {
//                view.setTextColor(context.resources.getColor(colors[0]))
//            } else {
//                view.setTextColor(context.resources.getColor(colors[1]))
//            }
        }

        if(textSize != 0){
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        }
        return view
    }



}