package com.example.login.fragments


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.login.R
import com.example.login.activities.MainActivity
import com.example.login.configuration.Vehicle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class OpenVehiclesFragment : Fragment() {

    var reg_button:Button? = null

    var OpenVehiclesFragmentListener: onOpenVehilcleFragmentListner? = null

    interface onOpenVehilcleFragmentListner{
        fun registerVehicle()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_vehicles, container, false)


        reg_button=view.findViewById(R.id.my_vehicle_register)
        reg_button?.setOnClickListener {
            OpenVehiclesFragmentListener?.registerVehicle()
        }
        getVehicle(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = context as Activity
        OpenVehiclesFragmentListener = activity as onOpenVehilcleFragmentListner
    }


    fun getVehicle(view: View){
        val linearLayout = view.findViewById<LinearLayout>(R.id.linearlayout)
        val call = MainActivity.apiInterface?.getVehicle(MainActivity.prefConfig?.readUsername())
        call?.enqueue(object : Callback<List<Vehicle>> {
            override fun onResponse(call: Call<List<Vehicle>>, response: Response<List<Vehicle>>) {
                var list: List<Vehicle> = response.body()!!
                val vehicle = arrayOfNulls<String>(list.size)
                for (i in 0 until list.size) {
                    vehicle[i] = list.get(i).getVehicleNumber()
                    val editText = TextView(activity)
                    editText.id = i
                    editText.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    editText.gravity = Gravity.CENTER
                    editText.setTypeface(Typeface.DEFAULT_BOLD)
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
                    editText.setTextColor(Color.WHITE)
                    editText.setText("${vehicle[i]}")
                    try {
                        linearLayout.addView(editText)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<List<Vehicle>>, t: Throwable) {
                println("failed********")
                println(t)
            }
        })
    }
}