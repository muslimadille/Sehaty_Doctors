package com.muslim_adel.sehatydoctors.modules.base




import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.muslim_adel.sehatydoctors.R

class CustomTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    lateinit var title: TextView


    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_tab_layout, this, true)
        title = view.findViewById(R.id.layout_text)


    }

    fun setTitle(count: String?) {
        title.text = count
    }

    fun getTitle(): String? = title.text.toString()

}