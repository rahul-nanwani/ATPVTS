package com.example.login.activities

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions




class GpsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var mapFragment: SupportMapFragment? = null
    var mMap: GoogleMap? = null
    var marker: Marker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_map_activity)
        setLocation()
    }

    override fun onResume() {
        super.onResume()
        setLocation()
    }

    override fun onRestart() {
        super.onRestart()
        setLocation()
    }

    fun setLocation(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    var lat: Double = 18.513858
                    var longitude: Double = 73.7721934
                    if(location != null){
                        lat = location!!.latitude
                         longitude = location!!.longitude
                    }
                    if (mMap != null) {
                        val latLng = LatLng(lat, longitude)
                        val markerOptions = MarkerOptions()
                        markerOptions.position(latLng)
                        if (marker != null) marker!!.position = latLng else marker = mMap!!.addMarker(markerOptions)
                        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
                    }

                    Toast.makeText(this@GpsActivity, "Latitude is: $lat, Longitude is $longitude", Toast.LENGTH_LONG).show()
                }

        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFrag) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

}