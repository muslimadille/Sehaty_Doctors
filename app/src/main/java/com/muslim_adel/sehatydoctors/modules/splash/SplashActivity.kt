package com.muslim_adel.sehatydoctors.modules.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.base.BaseActivity
import com.muslim_adel.sehatydoctors.modules.home.MainActivity
import com.muslim_adel.sehatydoctors.modules.introwizerd.IntroWizardActivity
import com.muslim_adel.sehatydoctors.modules.registration.LoginActivity
import com.muslim_adel.sehatydoctors.modules.registration.SelectUserActivity
import com.muslim_adel.sehatydoctors.utiles.Q
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