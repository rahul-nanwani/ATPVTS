package com.example.login.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.example.login.activities.MainActivity
import com.example.login.R
import com.example.login.configuration.NetworkUtil
import com.example.login.configuration.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    //Initialize Input text fields and buttons
    private var Name: EditText? = null
    private var UserName: EditText? = null
    private var UserPassword: EditText? = null
    private var BnRegister: AppCompatImageButton? = null
    private var login_button: AppCompatButton? = null

    var registerFormActivityListener: onRegisterFormActivityListener? = null

    interface onRegisterFormActivityListener { //This interface is implemented in main method
        fun performLoginRet()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        //find all views and attach to respective variable
        Name = view.findViewById(R.id.txt_name)
        UserName = view.findViewById(R.id.txt_username)
        UserPassword = view.findViewById(R.id.txt_password)
        login_button = view.findViewById(R.id.login_button)
        BnRegister = view.findViewById(R.id.bn_register)

        //Set on click listener for register button to perform register
        BnRegister?.setOnClickListener(View.OnClickListener {
            //perform all validations on input fields
            var status_internet = NetworkUtil.getConnectivityStatusString(context)
            if(status_internet)
            {
                if (Name?.length()!! < 4) {
                    Name?.requestFocus()
                    Name?.setError("Name should be more than 3 characters")
                } else if(!isNameValid(Name?.getText().toString()))
                {
                    Name?.requestFocus()
                    Name?.setError("Only Alphabets and one space is allowed")
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(UserName?.getText().toString()).matches())
                {
                    UserName?.requestFocus()
                    UserName?.setError("Enter Valid Email Address")
                }
                else if (UserPassword?.length()!! < 8) {
                    UserPassword?.requestFocus()
                    UserPassword?.setError("Password Should be greater than 8 chars")
                }
                else
                //if all validations are correct call registration function
                    performRegistration()
            }
            else if(!status_internet)
                MainActivity.prefConfig?.displayToast("No internet Connection")
            else
                MainActivity.prefConfig?.displayToast("Something Went Wrong")
        })
        //Set on click listener on login button to call login fragment
        login_button?.setOnClickListener(View.OnClickListener { registerFormActivityListener!!.performLoginRet() })
        return view
    }

    override fun onAttach(context: Context) {
        //this function is called when fragment is attached with activity
        super.onAttach(context)
        val activity = context as Activity
        registerFormActivityListener = activity as onRegisterFormActivityListener
    }

    fun performRegistration() {
        //get parameters from input textboxes
        val name = Name!!.text.toString()
        val username = UserName!!.text.toString()
        val password = UserPassword!!.text.toString()

        //retrofit call , pass name , username and password to server
        val call = MainActivity.apiInterface!!.performRegistration(name, username, password)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) { //on server response
                if (response.body()!!.response == "ok") {
                    MainActivity.prefConfig!!.displayToast("Registration Success")
                    registerFormActivityListener!!.performLoginRet()
                }
                else if (response.body()!!.response == "exist") {
                    MainActivity.prefConfig!!.displayToast("User Already Exists(Use different mail-id)")
                    registerFormActivityListener!!.performLoginRet()
                }
                else if (response.body()!!.response == "error") {
                    MainActivity.prefConfig!!.displayToast("Registration Failed")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                MainActivity.prefConfig?.displayToast("Server Down")
            } //on server unresponsive
        })
        //Clear values of text box inputs
        Name!!.setText("")
        UserName!!.setText("")
        UserPassword!!.setText("")
    }

    //this functions checks valid names
    fun isNameValid(name: String): Boolean {
        val NAME_REGEX = "^[a-zA-Z]+[ ]{0,1}[a-zA-Z]+\$"
        return NAME_REGEX.toRegex().matches(name)
    }
}
