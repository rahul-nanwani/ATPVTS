package com.example.login.fragments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageButton
import com.example.login.R
import com.example.login.activities.MainActivity
import com.example.login.configuration.ServerResponse
import com.example.login.configuration.Vehicle
import kotlinx.android.synthetic.main.fragment_register_vehicle.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class RegisterVehicleFragment : Fragment() {

    private var Veh_Num: EditText? = null
    private var Veh_Chass: EditText? = null
    private var Veh_Register: AppCompatImageButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_register_vehicle, container, false)
        Veh_Num = view.findViewById(R.id.veh_num)
        Veh_Chass = view.findViewById(R.id.veh_chass)
        Veh_Register = view.findViewById(R.id.veh_submit)
        Veh_Register?.setOnClickListener(View.OnClickListener {

            if (Veh_Num?.length() != 10) {
                Veh_Num?.requestFocus()
                Veh_Num?.setError("Vehicle Number should be of 10 characters")
            }
            else if (!isNumValid(Veh_Num!!.text.toString())) {
                Veh_Num?.requestFocus()
                Veh_Num?.setError("Enter Valid Vehicle Registration number")
            }
            else if (Veh_Chass!!.length() <= 10){
                Veh_Chass?.requestFocus()
                Veh_Chass?.setError("Invalid : Enter Valid Chassis Number")
            }
            else
                performVehicleRegistration()
        })
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = context as Activity
    }

    fun performVehicleRegistration() {
        val vehicleNumber = Veh_Num!!.text.toString()
        //println(vehicleNumber)
        val vehicleType = Veh_Chass!!.text.toString()
        //println(vehicleType)
        val username= MainActivity.prefConfig?.readUsername()
        //println(username)
        val call = MainActivity.apiInterface!!.performVehicleRegistration(username,vehicleNumber,vehicleType)
        call?.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                if (response.body()!!.response == "ok") {
                    MainActivity.prefConfig!!.displayToast("Vehicle Registration Success")
                } else if (response.body()!!.response == "exist") {
                    MainActivity.prefConfig!!.displayToast("Vehicle Already Exists")
                } else if (response.body()!!.response == "error") {
                    MainActivity.prefConfig!!.displayToast("Registration Failed")
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {}
        })

        Veh_Num!!.setText("")
        Veh_Chass!!.setText("")
    }


    fun isNumValid(name: String): Boolean {
        val NAME_REGEX = "^[A-Za-z]{2,2}[0-9]{2,2}[A-Za-z]{2,2}[0-9]{4,4}"
        return NAME_REGEX.toRegex().matches(name)
    }
}
