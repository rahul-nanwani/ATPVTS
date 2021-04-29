package com.example.login.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.login.R
import com.example.login.activities.MainActivity
import com.example.login.configuration.Bill
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaidFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_paid, container, false)
        getPaid(view)
        return view
    }



    fun getPaid(view: View){
        val linearLayout = view.findViewById<LinearLayout>(R.id.linearLayoutPaid)
        val call = MainActivity.apiInterface?.getPaidBills(MainActivity.prefConfig?.readUsername())
        call?.enqueue(object : Callback<List<Bill>> {
            override fun onResponse(call: Call<List<Bill>>, response: Response<List<Bill>>) {
                var list: List<Bill> = response.body()!!
                val bill = arrayOfNulls<String>(list.size)
                for (i in 0 until list.size) {
                    list.get(i).getBillId()
                    val editText = TextView(activity)
                    editText.id = i
                    val lparam = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    lparam.setMargins(0,30,0,30)
                    editText.layoutParams = lparam
                    editText.gravity = Gravity.LEFT
                    editText.setTypeface(Typeface.DEFAULT_BOLD)
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27f)
                    editText.setTextColor(Color.WHITE)
                    editText.setText("${list.get(i).getBillId()}  ${list.get(i).getVehicleNumber()}    ${list.get(i).getHighwayNumber()}       ${list.get(i).getToll()}")
                    try {
                        linearLayout.addView(editText)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<List<Bill>>, t: Throwable) {
                println("failed********")
                println(t)
            }
        })
    }
}