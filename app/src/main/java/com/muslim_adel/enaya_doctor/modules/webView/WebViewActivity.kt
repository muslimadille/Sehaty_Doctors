package com.muslim_adel.enaya_doctor.modules.webView

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.newRegistration.doctor.DoctorRegistrationScreen
import com.muslim_adel.enaya_doctor.modules.newRegistration.labs.LabRegisterationActivity
import com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.PharmRegisterationActivity
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : BaseActivity() {
    var key=0
    var urlString=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        key=intent.getIntExtra("key",0)
        when(key){
            0->{
                urlString="https://www.doctor.enaya.care/#/register"
            }
            1->{
                urlString="https://www.pharmacy.enaya.care/#/register"

            }
            2->{
                urlString="https://www.laboratory.enaya.care/#/register"
            }
        }

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.domStorageEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.setSupportZoom(true)

        // this will load the url of the website
        webView.loadUrl(urlString)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                // Page loading started
                // Do something
                if(!url.contains("register")){
                    finish()
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                // Page loading finished
                // Enable disable back forward button
            }
        }

        // if you want to enable zoom feature

    }
}