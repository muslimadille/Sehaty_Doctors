package com.muslim_adel.enaya_doctor.modules.base

import android.app.Application
import com.muslim_adel.enaya_doctor.utiles.ComplexPreferences
import com.muslim_adel.enaya_doctor.utiles.Q
import java.util.*

class App : Application() {
    var preferences: ComplexPreferences? = null

    override fun onCreate() {
        super.onCreate()

        var change = ""
        preferences = ComplexPreferences.getComplexPreferences(this, Q.PREF_FILE, Q.MODE_PRIVATE)
        val language = preferences!!.getString("language", "en")
        if (language =="Arabic") {
            change="ar"
        } else if (language=="English" ) {
            change = "en"
        }else if(language=="Kurdish") {
            change ="ur"
        }

        BaseActivity.dLocale = Locale(change) //set any locale you want here
    }
}