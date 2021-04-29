package com.example.login.activities


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.login.R
import com.example.login.configuration.ApiClient.getApiClient
import com.example.login.configuration.ApiInterface
import com.example.login.configuration.PrefConfig
import com.example.login.fragments.LoginFragment
import com.example.login.fragments.LoginFragment.onLoginFormActivityListener
import com.example.login.fragments.RegisterFragment
import com.example.login.fragments.RegisterFragment.onRegisterFormActivityListener


class MainActivity : AppCompatActivity(), onLoginFormActivityListener , onRegisterFormActivityListener//,OnLogoutListener
    {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            println("Main Started")
        prefConfig = PrefConfig(this) // This Creates Preferences for login
        apiInterface = getApiClient()!!.create(ApiInterface::class.java)  //this creates apiInterface of retrofit

           if (findViewById<View?>(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return          //Activity generation failed
            }
            if (prefConfig?.readLoginStatus().equals("loggedIn")) {    //if already login
                if(prefConfig!!.readLock()!=null){
                    val intent = Intent(this, LockActivity::class.java)
                    startActivity(intent)
                    this.finish()//Start DashboardFragment Activity
                    println("lock Started after start")
                }
                else
                {
                    val intent = Intent(this, Dashboard_Activity::class.java)
                    startActivity(intent) //Start DashboardFragment Activity
                    this.finish()
                    println("Dash Started after start")
                }
            } else { //if not login open login fragment
                supportFragmentManager.beginTransaction().add(R.id.fragment_container, LoginFragment()).commit()
            }
        }
    }
        //****************************************************************************************************************
       //Login Fragment Activity Listener
       override fun performRegister() {    //This function Calls Register fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, RegisterFragment()).addToBackStack(null).commit()
       }

        override fun performLogin(username: String?,name:String?) { //This function perform login task outside login fragment
            prefConfig!!.writeUsername(username)  //write login details to preferences
            prefConfig!!.writeName(name)
            println("here")
            val intent = Intent(this, Dashboard_Activity::class.java)
            startActivity(intent) //start DashboardFragment activity
            this.finish()
            println("Dash Started after perflogin")
         }


         //******************************************************************************************************************************
         //Register Fragment Activity Listener
         override fun performLoginRet(){   // this function changes fragment to login fragment
             supportFragmentManager.popBackStackImmediate()  //pops register fragment from stack
         }

         /*
    override fun logoutPerformed() {
        prefConfig!!.writeLoginStatus(false)
        prefConfig!!.writeName("User")
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, LoginFragment()).commit()
    }
        */

    //This object is use to create static values
    companion object {
        @JvmField
        var prefConfig: PrefConfig? = null    //only one preferences file
        @JvmField
        var apiInterface: ApiInterface? = null   //only one interface object
    }
}