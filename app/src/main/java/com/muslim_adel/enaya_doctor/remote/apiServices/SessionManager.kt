package com.muslim_adel.enaya_doctor.remote.apiServices
import android.content.Context
import android.content.SharedPreferences
import com.muslim_adel.enaya_doctor.R

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "tok"
        const val COUNTRY_ID = "COUNTRY_ID"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String,countryId:Int) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putInt(COUNTRY_ID,countryId)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
    fun fetchCountryId(): Int {
        return prefs.getInt(COUNTRY_ID, 1)
    }
}