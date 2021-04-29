package com.example.login.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.login.activities.LockActivity
import com.google.android.gms.location.*

class LocationService : Service() {
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var locationCallback: LocationCallback? = null
    var prevLattitude: Double = 0.000
    var prevLongitude: Double = 0.000
    var result = FloatArray(2)
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (prevLattitude != 0.000){
                    Location.distanceBetween(prevLattitude, prevLongitude, locationResult.lastLocation.latitude, locationResult.lastLocation.longitude, result)
                }else {
                    result[0] = (-1).toFloat()
                }
                println(result[0])
                if (result[0] > 0 || result[0]== (-1).toFloat()) {
                    Log.d("mylog", "Lat is: " + locationResult.lastLocation.latitude + ", "
                            + "Lng is: " + locationResult.lastLocation.longitude)
                    val intent = Intent("ACT_LOC")
                    intent.putExtra("latitude", locationResult.lastLocation.latitude)
                    intent.putExtra("longitude", locationResult.lastLocation.longitude)
                    sendBroadcast(intent)
                    prevLattitude = locationResult.lastLocation.latitude;
                    prevLongitude = locationResult.lastLocation.longitude;
                }
            }
        }
        startForeground(12345678, notification)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        requestLocation()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun restartLockActivity(){
        val dialogIntent = Intent(this, LockActivity::class.java)
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(dialogIntent)
    }

    private fun requestLocation() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 1
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private val notification: Notification
        private get() {
            val channel = NotificationChannel(
                    "channel_01",
                    "My Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
            val builder = Notification.Builder(
                    applicationContext, "channel_01"
            ).setAutoCancel(true)
            return builder.build()
        }
}
