package com.seha_khanah_doctors.modules.map

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*

class MapsActivity : BaseActivity(), OnMapReadyCallback {
    var name=""
    var lat="3.000"
    var lng="2.000"

    private lateinit var mMap: GoogleMap
    var fusedLocationProviderClient:FusedLocationProviderClient?=null
    var currentLocation:Location?=null
    var currentMrker:Marker?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

    }

    private fun fetchLocation(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
            &&ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return
        }
        val task=fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location->
            if(location!=null){
                this.currentLocation=location
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            1000->{if(grantResults.size!=0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                fetchLocation()
            }}
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val lat_long=LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!)
        drawMarker(lat_long)
        mMap.setOnMarkerDragListener(object :GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragStart(p0: Marker?) {
            }

            override fun onMarkerDrag(p0: Marker?) {
            }

            override fun onMarkerDragEnd(p0: Marker?) {

                if(currentMrker!=null){
                    currentMrker?.remove()
                    var newLatLng=LatLng(p0?.position!!.latitude,p0.position.longitude)
                    drawMarker(newLatLng)

                }
            }
        })

    }
    private fun drawMarker(lat_long:LatLng){
       val markerOption= MarkerOptions().position(lat_long).snippet(getAddress(lat_long.latitude,lat_long.latitude)).draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(lat_long))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_long,15f))
        currentMrker=mMap.addMarker(markerOption)
        currentMrker?.showInfoWindow()
        edit_location_txt.text=getAddress(lat_long.latitude,lat_long.longitude).toString()
        lat=lat_long.latitude.toString()
        lng=lat_long.latitude.toString()

    }
    private  fun getAddress(lat:Double,lng:Double):String{
       val getCoder= Geocoder(this,Locale.getDefault())
        val address=getCoder.getFromLocation(lat,lng,1)
        return address[0].getAddressLine(0).toString()
    }
}