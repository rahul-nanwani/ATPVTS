package com.example.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.login.R
import com.example.login.activities.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class payBillFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_pay_bill, container, false)
        var payBillButton:AppCompatButton?= null
        payBillButton=view.findViewById(R.id.payBillButton)
        payBillButton?.setOnClickListener(View.OnClickListener {
            MainActivity.prefConfig?.displayToast("Proceed To Bank Interface")
            getActivity()?.onBackPressed()
        })
        return view
    }
}