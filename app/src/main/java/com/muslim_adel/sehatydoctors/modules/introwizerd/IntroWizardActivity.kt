package com.muslim_adel.sehatydoctors.modules.introwizerd

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.muslim_adel.sehatydoctors.modules.home.MainActivity
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.base.BaseActivity
import com.muslim_adel.sehatydoctors.modules.introwizerd.adapters.IntroPagerAdapter
import com.muslim_adel.sehatydoctors.modules.introwizerd.fragments.Intro1Fragment
import com.muslim_adel.sehatydoctors.modules.introwizerd.fragments.Intro2Fragment
import com.muslim_adel.sehatydoctors.modules.introwizerd.fragments.Intro3Fragment
import com.muslim_adel.sehatydoctors.modules.registration.LoginActivity
import com.muslim_adel.sehatydoctors.utiles.Q
import kotlinx.android.synthetic.main.activity_intro_wizard.*
import java.util.*
import kotlin.collections.ArrayList

class IntroWizardActivity :BaseActivity() {
    private val fragmentList = ArrayList<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_wizard)

        // making the status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if(intent.getBooleanExtra("key",false)){
            intro_register_btn.visibility= View.GONE
        }

        val adapter = IntroPagerAdapter(this)
        vpIntroSlider.adapter = adapter
        fragmentList.addAll(listOf(
            Intro1Fragment(), Intro2Fragment(), Intro3Fragment()
        ))
        adapter.setFragmentList(fragmentList)

        val handler = Handler()
        val update = Runnable {
            if (vpIntroSlider.getCurrentItem() == 0) {
                vpIntroSlider.currentItem = 1
            } else if (vpIntroSlider.getCurrentItem() == 1) {
                vpIntroSlider.currentItem = 2
            } else {
                vpIntroSlider.currentItem = 0
            }

        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 3500, 3500)

        intro_start_btn.setOnClickListener {
            preferences!!.putBoolean(Q.IS_FIRST_TIME, false)
            preferences!!.commit()
            val intent = Intent(this@IntroWizardActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        intro_register_btn.setOnClickListener {
            preferences!!.putBoolean(Q.IS_FIRST_TIME, false)
            preferences!!.commit()
            val intent = Intent(this@IntroWizardActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


}