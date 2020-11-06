package com.muslim_adel.sehatydoctors.modules.base

import android.content.DialogInterface
import android.content.res.Configuration
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.utiles.ComplexPreferences
import com.muslim_adel.sehatydoctors.utiles.Q
import java.lang.Error
import java.util.*

open class BaseActivity : AppCompatActivity() {
    var preferences: ComplexPreferences? = null

    companion object {
        public var dLocale: Locale? = null

    }

    init {
        preferences = ComplexPreferences.getComplexPreferences(this, Q.PREF_FILE, Q.MODE_PRIVATE)
        updateConfig(this)
    }

    fun updateConfig(wrapper: ContextThemeWrapper) {
        if(dLocale==Locale("") ) // Do nothing if dLocale is null
            return

        Locale.setDefault(dLocale)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        wrapper.applyOverrideConfiguration(configuration)
    }
    open fun alertNetwork(isExit: Boolean = false) {
        val alertBuilder = AlertDialog.Builder(this)
        //alertBuilder.setTitle(R.string.error)
        alertBuilder.setMessage(R.string.no_internet)
        if (isExit) {
            alertBuilder.setPositiveButton(R.string.exit) { dialog: DialogInterface, _: Int -> finish() }
        } else {
            alertBuilder.setPositiveButton(R.string.dismiss) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        }
        try {
            if(!this.isFinishing){
                alertBuilder.show()
            }

        }catch (e:Error){}
    }
}