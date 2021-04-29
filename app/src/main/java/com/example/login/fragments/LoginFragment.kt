package com.example.login.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.example.login.activities.MainActivity
import com.example.login.R
import com.example.login.configuration.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.login.configuration.NetworkUtil.getConnectivityStatusString


class LoginFragment : Fragment() {
    //Initialize Input text fields and buttons
    private var RegButton: AppCompatButton? = null
    private var Username: EditText? = null
    private var Password: EditText? = null
    private var LoginButton: AppCompatImageButton? = null

    var loginFormActivityListener: onLoginFormActivityListener? = null

    interface onLoginFormActivityListener {   //interface implemented in main activity
        fun performRegister()
        fun performLogin(username: String?,name: String?)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        //find all views and attach to respective variable
        RegButton = view.findViewById(R.id.register_button)
        Username = view.findViewById(R.id.user_name)
        Password = view.findViewById(R.id.user_pass)
        LoginButton = view.findViewById(R.id.login_bn)

        //Set on click listener for login button to perform login
        LoginButton?.setOnClickListener(View.OnClickListener {
            //perform all validations on input fields
            var status_internet = getConnectivityStatusString(context)
            if(status_internet)
            {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(Username?.getText().toString()).matches())
                {
                    Username?.requestFocus()
                    Username?.setError("Enter Valid Email Address")
                }
                else if (Password?.length()!! < 8) {
                    Password?.requestFocus()
                    Password?.setError("Password Should be greater than 8 chars")
                }
                else
                //if all validations are correct call login function
                    performLogin()
            }
            else if(!status_internet)
                MainActivity.prefConfig?.displayToast("No internet Connection")
            else
                MainActivity.prefConfig?.displayToast("Something Went Wrong")
        })
        //Set on click listener on register button to call register fragment
        RegButton?.setOnClickListener(View.OnClickListener { loginFormActivityListener!!.performRegister() })
        return view
    }

    override fun onAttach(context: Context) {
        //this function is called when fragment is attached with activity
        super.onAttach(context)
        val activity = context as Activity
        loginFormActivityListener = activity as onLoginFormActivityListener //context is changed to login form fragment
    }


    fun performLogin() {
        //get parameters from input textboxes
        val username = Username!!.text.toString()
        val password = Password!!.text.toString()

        //retrofit call , pass username and password to server
        val call = MainActivity.apiInterface?.performUserLogin(username, password)
        call?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) { //on server response
                if (response.body()!!.response == "ok") {
                    println("login success")
                    //MainActivity.prefConfig?.displayToast("Login Success")
                    onCheckboxClicked(view!!.findViewById(R.id.checkbox)) //check if checkbox of remember me is clicked ior not
                    loginFormActivityListener?.performLogin(response.body()!!.username,response.body()!!.name)

                    //Clear values of text box inputs
                    Username!!.setText("")
                    Password!!.setText("")
                } else if (response.body()!!.response == "failed") {
                    println("login failed")
                    MainActivity.prefConfig?.displayToast("Incorrect User/Email/Password")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                println("Retrofit fail")
                MainActivity.prefConfig?.displayToast("Server Down")
                MainActivity.prefConfig?.displayToast("Server Down")
            }//on server failed to respond
        })
    }

    fun onCheckboxClicked(view: View) {//This checkbox is use to remember user login
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.checkbox -> {
                    if (checked) {
                        MainActivity.prefConfig?.writeLoginStatus("loggedIn")
                        return
                    }
                    else {
                        MainActivity.prefConfig?.writeLoginStatus("tempLogin")
                        return
                    }
                }

            }

        }
    }

}