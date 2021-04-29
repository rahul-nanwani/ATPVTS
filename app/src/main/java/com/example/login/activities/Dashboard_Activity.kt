package com.example.login.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.login.fragments.DashboardFragment
import com.example.login.fragments.OpenVehiclesFragment
import com.example.login.R
import com.example.login.fragments.OpenVehiclesFragment.onOpenVehilcleFragmentListner
import com.example.login.fragments.RegisterVehicleFragment

class Dashboard_Activity : AppCompatActivity() , DashboardFragment.onDashboardActivityListner , onOpenVehilcleFragmentListner{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        if (findViewById<View?>(R.id.fragment_container2) != null) {
            if (savedInstanceState != null) {
                return
            }
            if (MainActivity.prefConfig!!.readLoginStatus()!="loggedOut") {
                supportFragmentManager.beginTransaction().add(R.id.fragment_container2, DashboardFragment()).commit()
            } else {
                finish()
            }
        }
    }

    override fun open_vehicle() {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container2, OpenVehiclesFragment()).addToBackStack(null).commit()
    }
    override fun open_realtime() {
        val intent = Intent(this, GpsActivity::class.java)
        startActivity(intent)
    }

    override fun exit() {
        if (MainActivity.prefConfig!!.readLoginStatus()=="tempLogin") {
            MainActivity.prefConfig?.writeLoginStatus("loggedOut")
        }
        finishAffinity()
    }

    override fun open_payments() {
        val intent = Intent(this, PaymentActivity::class.java)
        startActivity(intent)
    }

    override fun open_lock() {
        val intent = Intent(this, LockActivity::class.java)
        startActivity(intent)
    }

    override fun logout() {
        MainActivity.prefConfig!!.writeLoginStatus("loggedOut")
        MainActivity.prefConfig!!.writeName("User")
        finishAffinity()
    }

    override fun registerVehicle() {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container2, RegisterVehicleFragment()).addToBackStack(null).commit()
    }
}
