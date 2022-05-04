package com.muslim_adel.enaya_doctor.modules.settings

import android.content.Intent
import android.os.Bundle
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_change_language.*

class ChangeLanguageActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_language)
        setSavetValus()
        onSaveClicked()
    }
    private fun setSavetValus(){
        if (preferences!!.getString("language","")=="Arabic"){
            lang_name_txt.text="عربي"
            ar_check.isChecked=true
            kr_check.isChecked=false
            en_check.isChecked=false
        }else if(preferences!!.getString("language","")=="English"){
            lang_name_txt.text="English"
            ar_check.isChecked=false
            kr_check.isChecked=false
            en_check.isChecked=true
        }
        else{
            lang_name_txt.text="كردي"
            ar_check.isChecked=false
            kr_check.isChecked=true
            en_check.isChecked=false
        }
        ar_check.setOnClickListener {
            lang_name_txt.text="عربي"
            ar_check.isChecked=true
            en_check.isChecked=false
            kr_check.isChecked=false

        }
        kr_check.setOnClickListener {
            lang_name_txt.text="كردي"
            kr_check.isChecked=true
            ar_check.isChecked=false
            en_check.isChecked=false
        }
        en_check.setOnClickListener {
            lang_name_txt.text="English"
            ar_check.isChecked=false
            kr_check.isChecked=false
            en_check.isChecked=true
        }

    }
    private fun onSaveClicked(){
        lang_save_btn.setOnClickListener {
            if(ar_check.isChecked){
                preferences!!.putString("language", "Arabic")
                preferences!!.commit()
                val intent = Intent(this@ChangeLanguageActivity, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }else if(en_check.isChecked){
                preferences!!.putString("language", "English")
                preferences!!.commit()
                val intent = Intent(this@ChangeLanguageActivity, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                preferences!!.putString("language", "Kurdish")
                preferences!!.commit()
                val intent = Intent(this@ChangeLanguageActivity, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}