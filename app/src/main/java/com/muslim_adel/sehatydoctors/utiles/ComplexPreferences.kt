package com.muslim_adel.sehatydoctors.utiles

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.muslim_adel.sehatydoctors.modules.home.MainActivity

class ComplexPreferences @SuppressLint("CommitPrefEdits") constructor(context: Context, namePreferences: String?, mode: Int) {

    private val GSON = Gson()
    //private static Gson GSON = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).setPrettyPrinting().create();
    private var preferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var editorLocal: SharedPreferences.Editor? = null

    init {
        var myNamePreferences = namePreferences

        if (namePreferences == null || namePreferences == "") {
            myNamePreferences = "complex_preferences"
        }
        preferences = context.getSharedPreferences(myNamePreferences, mode)
        preferences.let { editor = it!!.edit() }
        preferences.let { editorLocal = it!!.edit() }
    }

    companion object {
        private var complexPreferences: ComplexPreferences? = null

        fun getComplexPreferences(
            context: Context?,
            namePreferences: String, mode: Int): ComplexPreferences {

            if (complexPreferences == null) {
                complexPreferences = context?.let {
                    ComplexPreferences(
                        it,
                        namePreferences, mode)
                }
            }

            complexPreferences.let { return it!! }
        }
    }

   // fun isAppEnglish(): Boolean = getInteger(Q.SELECTED_LOCALE_PREF, Q.LOCALE_AR_INDEX) == Q.LOCALE_EN_INDEX

    fun putObject(key: String, `object`: Any?) {
        if (`object` == null) {
            throw IllegalArgumentException("object is null")

        }

        if (key == "") {
            throw IllegalArgumentException("key is empty or null")
        }

        try {
            editor!!.putString(key, GSON.toJson(`object`))
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun <T> getObject(key: String, classItem: Class<T>): T? {
        val gson = preferences!!.getString(key, null)
        return if (gson == null) {
            null
        } else {
            try {
                GSON.fromJson(gson, classItem)
            } catch (e: Exception) {
                throw IllegalArgumentException("Object stored with key $key is instanceof other class")
            }

        }
    }

    fun putInteger(key: String, number: Int) {
        editor!!.putInt(key, number)
    }

    fun getInteger(key: String, number: Int): Int {
        return preferences!!.getInt(key, number)
    }

    fun putString(key: String, charSequence: String) {
        editor!!.putString(key, charSequence)
    }

    fun getString(key: String, charSequence: String): String {
        return preferences!!.getString(key, charSequence)!!
    }

    fun putLong(key: String, number: Long) {
        editor!!.putLong(key, number)
    }

    fun getLong(key: String, number: Long): Long {
        return preferences!!.getLong(key, number)
    }

    fun putBoolean(key: String, state: Boolean) {
        editor!!.putBoolean(key, state)
    }

    fun getBoolean(key: String, state: Boolean): Boolean {
        return preferences!!.getBoolean(key, state)
    }

    fun clearAllSettings() {
        editor!!.clear()
        editor!!.commit()
    }

    fun commit() {
        editor!!.commit()
    }
    fun isAppEnglish(): Boolean = getInteger(Q.SELECTED_LOCALE_PREF, Q.LOCALE_AR_INDEX) == Q.LOCALE_EN_INDEX

}