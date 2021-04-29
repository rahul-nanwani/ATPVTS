package com.example.login.activities

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.login.R
import com.example.login.configuration.*
import com.example.login.configuration.GpsUtils.onGpsListener
import com.example.login.services.LocationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp

var isGPS = false

class LockActivity : AppCompatActivity() {
    var receiver: LocationBroadcastReceiver? = null
    lateinit var mydb: DBHelper
    private var LockButton: AppCompatButton? = null
    private var TextLock: TextView? = null
    private var VehicleNumber: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)

        MainActivity.prefConfig = PrefConfig(this)
        VehicleNumber = findViewById(R.id.vehicle_id)
        LockButton= findViewById(R.id.buttonLock)
        TextLock=findViewById(R.id.textLock)

       if (MainActivity.prefConfig!!.readLock() != null) {
           LockButton?.setVisibility(View.GONE)
           TextLock?.setVisibility(View.VISIBLE)
           VehicleNumber?.setVisibility(View.GONE)
           TextLock?.setText("Locked as Vehicle " + MainActivity.prefConfig!!.readLock())
       }
        else {
           LockButton?.setVisibility(View.VISIBLE)
           TextLock?.setVisibility(View.GONE)
           VehicleNumber?.setVisibility(View.VISIBLE)
           LockButton?.setOnClickListener() {
           var status_internet = NetworkUtil.getConnectivityStatusString(this)
           if(status_internet)
           {
               if (VehicleNumber?.length() != 10) {
                   println("Vehicle Number length condition failed")
                   VehicleNumber?.requestFocus()
                   VehicleNumber?.setError("Vehicle Number should be of 10 characters")
               }
               else if (!isNumValid(VehicleNumber!!.text.toString())) {
                   println("Vehicle Number pattern condition failed")
                   VehicleNumber?.requestFocus()
                   VehicleNumber?.setError("Enter Valid Vehicle Registration number")
               }
               else {
                   val vehicleNumber = VehicleNumber!!.text.toString()
                   performLock(vehicleNumber)
               }
           }
           else if(!status_internet)
               MainActivity.prefConfig?.displayToast("No internet Connection")
           else
               MainActivity.prefConfig?.displayToast("Something Went Wrong")

           }
        }
        receiver = LocationBroadcastReceiver()
        startLocService()
        mydb = DBHelper(this)
        pushData()
    }

    fun pushData() {
        locationPerm()
        GpsUtils(this).turnGPSOn(object : onGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                // turn on GPS
                isGPS = isGPSEnable
            }
        })
        var locationIds = mydb.allLocation
        for (i in locationIds.indices) {
            println("location ID :" + locationIds[i])
            var res = mydb.getData(locationIds[i].toInt())
            res.moveToFirst()
            var lat = res.getString(res.getColumnIndex("lat"))
            var lon = res.getString(res.getColumnIndex("lan"))
            var vehicleNumber = res.getString(res.getColumnIndex("vehicle"))
            var timestamp = Timestamp.valueOf(res.getString(res.getColumnIndex("timestamp")))
            var status_internet = NetworkUtil.getConnectivityStatusString(this)
            if(status_internet){
                if(saveLocationTime(lon, lat, vehicleNumber, timestamp)) {
                    println("deleting location id" + locationIds[i])
                    mydb.deleteLocation(locationIds[i].toInt())
                }
            } else {
                println("No internet : DB Intact")
            }
        }
        refresh()
    }
    fun refresh(){
        val handler = Handler()
        val timedTask: Runnable = object : Runnable {
            override fun run() {
                pushData()
            }
        }
        handler.postDelayed(timedTask, 5000)
    }

    fun locationPerm(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION), 1)
            }
        }
    }
    fun performLock(vehicleNumber: String) {
        println("Performing vehicle lock")
        LockButton?.setVisibility(View.GONE)
        TextLock?.setVisibility(View.VISIBLE)
        VehicleNumber?.setVisibility(View.GONE)
        TextLock?.setText("Locked as Vehicle " + vehicleNumber)
        MainActivity.prefConfig?.writeLock(vehicleNumber)
    }

     fun undoLock() {
        println("Performing undo lock")
        LockButton?.setVisibility(View.VISIBLE)
        TextLock?.setVisibility(View.GONE)
        VehicleNumber?.setVisibility(View.VISIBLE)
        MainActivity.prefConfig?.writeLock(null)
    }

    fun startLocService() {
        val filter = IntentFilter("ACT_LOC")
        registerReceiver(receiver, filter)
        val intent = Intent(this@LockActivity, LocationService::class.java)
        startService(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
       try {
           unregisterReceiver(receiver)
       }
       catch (e: Exception)
       {
           print(e.stackTrace)
       }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(MainActivity.prefConfig!!.readLock() != null)
            this.finish()
    }

    inner class LocationBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "ACT_LOC") {
                val lat = intent.getDoubleExtra("latitude", 0.0)
                val longitude = intent.getDoubleExtra("longitude", 0.0)
                val vehicle = MainActivity.prefConfig!!.readLock()
                if(vehicle != null){
                    var status_internet = NetworkUtil.getConnectivityStatusString(this@LockActivity)
                    if(!status_internet) {
                        MainActivity.prefConfig!!.displayToast("No Internet : Saving Location")
                        mydb.insertLocation(lat.toString(), longitude.toString(), vehicle)
                    } else
                    saveLocation(lat, longitude, vehicle)
                }
                Toast.makeText(this@LockActivity, "Latitude is: $lat, Longitude is $longitude", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveLocation(lon: Double, lat: Double, vehicle: String) {
        val lonStr : String =lon.toString()
        val latStr : String =lat.toString()
        val call = MainActivity.apiInterface!!.submitLocation(lonStr, latStr, vehicle)
        call.enqueue(object : Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) { //on server response
                if (response.body() != null) {
                    if (response.body()?.response == "ok") {
                        MainActivity.prefConfig!!.displayToast("Submit Success")
                    } else if (response.body()!!.response == "error") {
                        MainActivity.prefConfig!!.displayToast("Submit Failed")
                        mydb.insertLocation(latStr, lonStr, vehicle)
                    } else if (response.body()!!.response == "noVehicle") {
                        undoLock()
                        MainActivity.prefConfig!!.displayToast("Vehicle not verified")
                    }
                }
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {} //on server unresponsive
        })

    }

    fun saveLocationTime(lon: String, lat: String, vehicle: String, time: Timestamp): Boolean {
        val call = MainActivity.apiInterface!!.submitLocationTime(lon, lat, vehicle, time)
        var status = true
        call.enqueue(object : Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) { //on server response
                if (response.body() != null) {
                    if (response.body()!!.response == "ok") {
                        MainActivity.prefConfig!!.displayToast("Submit Success after db")
                    } else if (response.body()!!.response == "error") {
                        MainActivity.prefConfig!!.displayToast("Submit Failed after db")
                        status = false
                    } else if (response.body()!!.response == "noVehicle") {
                        undoLock()
                        MainActivity.prefConfig!!.displayToast("Vehicle not verified")
                    }
                }
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {} //on server unresponsive
        })
        return status
    }

    fun isNumValid(name: String): Boolean {
        val NAME_REGEX = "^[A-Za-z]{2,2}[0-9]{2,2}[A-Za-z]{2,2}[0-9]{4,4}"
        return NAME_REGEX.toRegex().matches(name)
    }

}

