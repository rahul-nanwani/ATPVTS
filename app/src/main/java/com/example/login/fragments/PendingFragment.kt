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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.login.R
import com.example.login.activities.MainActivity
import com.example.login.configuration.Bill
import com.example.login.configuration.Vehicle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingFragment : Fragment() {

    var OpenPendingFragmentListener: onOpenPendingFragmentListner? = null

    interface onOpenPendingFragmentListner{
        fun payBill(billId : String?,vehicleNumber : String?,highwayNumber : String?,totalKm : String?,toll : String?)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pending, container, false)
        getPending(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = context as Activity
        OpenPendingFragmentListener = activity as PendingFragment.onOpenPendingFragmentListner
    }



    fun getPending(view: View){
        val linearLayout = view.findViewById<LinearLayout>(R.id.linearLayoutPending)
        val call = MainActivity.apiInterface?.getPendingBills(MainActivity.prefConfig?.readUsername())
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
                    editText.setText("${list.get(i).getBillId()}   ${list.get(i).getVehicleNumber()}    ${list.get(i).getHighwayNumber()}      ${list.get(i).getToll()}")
                    try {
                        linearLayout.addView(editText)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    editText.setOnClickListener {
                        OpenPendingFragmentListener?.payBill(list.get(i).getBillId(),list.get(i).getVehicleNumber(), list.get(i).getHighwayNumber(),list.get(i).getKm(),list.get(i).getDate())
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