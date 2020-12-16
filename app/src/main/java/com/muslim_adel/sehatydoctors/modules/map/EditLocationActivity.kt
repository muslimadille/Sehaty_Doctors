package com.seha_khanah_doctors.modules.map

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_location.*

class EditLocationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_location)
        edit_location_txt.setOnClickListener {
            var builder=PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this),0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0&&resultCode==Activity.RESULT_OK){
            var place:Place=PlacePicker.getPlace(this,data)
            edit_location_txt.text=place.latLng.latitude.toString()
        }
    }
}