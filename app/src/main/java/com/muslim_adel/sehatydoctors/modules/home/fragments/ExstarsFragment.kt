package com.seha_khanah_doctors.modules.home.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.modules.settings.ChangeLanguageActivity
import com.seha_khanah_doctors.modules.settings.ContactUsActivity
import com.seha_khanah_doctors.modules.splash.SplashActivity
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.extras_fragment.*


class ExstarsFragment : Fragment() {
    private lateinit var sessionManager: SessionManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.extras_fragment, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sessionManager = SessionManager(mContext!!)
        onLogoutClicked()
        onChangeLanguageClicked()
        onContactUsClicked()


    }


    var mContext: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }
    override fun onDetach() {
        super.onDetach()
        mContext = context as MainActivity
    }
    private fun onLogoutClicked(){
        logout_btn.setOnClickListener {
            sessionManager.saveAuthToken("",0)
            mContext!!.preferences!!.putInteger("COUNTRY_ID",0)
            mContext!!.preferences!!.putBoolean(Q.IS_FIRST_TIME,true)
            mContext!!.preferences!!.putBoolean(Q.IS_LOGIN,false)
            mContext!!.preferences!!.putInteger(Q.USER_ID,-1)
            mContext!!.preferences!!.putString(Q.USER_NAME,"")
            mContext!!.preferences!!.putString(Q.USER_EMAIL,"")
            mContext!!.preferences!!.putString(Q.USER_PHONE,"")
            mContext!!.preferences!!.putInteger(Q.USER_GENDER,-1)
            mContext!!.preferences!!.commit()
            val intent = Intent(mContext, SplashActivity::class.java)
            startActivity(intent)
            mContext!!.finish()
        }
    }
    private  fun onChangeLanguageClicked(){
        stn_language_btn.setOnClickListener {
            val intent = Intent(mContext, ChangeLanguageActivity::class.java)
            startActivity(intent)
        }
    }
    private fun onContactUsClicked(){
        help_btn.setOnClickListener {
            val intent = Intent(context, ContactUsActivity::class.java)
            startActivity(intent)
        }
    }
}