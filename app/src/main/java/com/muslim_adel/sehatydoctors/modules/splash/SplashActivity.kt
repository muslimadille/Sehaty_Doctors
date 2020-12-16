package com.seha_khanah_doctors.modules.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.modules.introwizerd.IntroWizardActivity
import com.seha_khanah_doctors.modules.registration.LoginActivity
import com.seha_khanah_doctors.modules.registration.SelectUserActivity
import com.seha_khanah_doctors.utiles.Q
import java.util.*

class SplashActivity : BaseActivity() {
    private var change=""
    private var isLogin=false
    var isFristTime=true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        isLogin=preferences!!.getBoolean(Q.IS_LOGIN,false)
        isFristTime=preferences!!.getBoolean(Q.IS_FIRST_TIME,true)
        isFristTime=preferences!!.getBoolean(Q.IS_FIRST_TIME, Q.FIRST_TIME)
        
        setLocalization()
        handelSpalash()

    }
    private fun setLocalization(){
        val language = preferences!!.getString("language", "en")
        if (language =="Arabic") {
            change="ar"
        } else if (language=="English" ) {
            change = "en"
        }else {
            change =""
        }
        dLocale = Locale(change) //set any locale you want here

    }
    private fun handelSpalash(){

        Handler().postDelayed({
            if (isFristTime) {
                preferences!!.putString("language", "Arabic")
                preferences!!.commit()
                val intent = Intent(this@SplashActivity, SelectUserActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                if(isLogin){
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this@SplashActivity, SelectUserActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 2000)

    }
}