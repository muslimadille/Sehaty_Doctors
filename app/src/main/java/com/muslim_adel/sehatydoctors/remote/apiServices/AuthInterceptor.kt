package com.seha_khanah_doctors.remote.apiServices

import android.content.Context
import com.seha_khanah_doctors.utiles.ComplexPreferences
import com.seha_khanah_doctors.utiles.Q
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(context: Context) : Interceptor {
    var preferences: ComplexPreferences? = null
    var mcontext=context


    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        preferences = ComplexPreferences.getComplexPreferences(mcontext, Q.PREF_FILE, Q.MODE_PRIVATE)
        val requestBuilder = chain.request().newBuilder()

        if(sessionManager.fetchAuthToken()==null){
            var token=preferences!!.getString("tok","")
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }else{
            sessionManager.fetchAuthToken()?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }

            //handle country id
            if(sessionManager.fetchCountryId()==null){
                var countryId=preferences!!.getInteger("COUNTRY_ID",1)
                requestBuilder.addHeader("Country-id", countryId.toString())
            }else{
                sessionManager.fetchCountryId()?.let {
                    requestBuilder.addHeader("Country-id",it.toString())
                }
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}