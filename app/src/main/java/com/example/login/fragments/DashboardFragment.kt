package com.example.login.fragments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import com.example.login.R
import com.example.login.activities.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {
    private var card_vehicles : CardView? = null
    private var card_realtime : CardView? = null
    private var card_payment : CardView? = null
    private var card_lock : CardView? = null
    private var card_exit : CardView? = null
    private var textdash: TextView? = null
    private var logout: AppCompatImageButton? = null

    var DashboardActivityListener: onDashboardActivityListner? = null

    interface onDashboardActivityListner{
        fun open_vehicle()
        fun open_realtime()
        fun open_payments()
        fun open_lock()
        fun exit()
        fun logout()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        textdash = view?.findViewById(R.id.textdashuser)
        textdash?.setText("Welcome ${MainActivity.prefConfig?.readName()}")

        card_vehicles = view?.findViewById(R.id.card_vehicles)
        card_realtime = view?.findViewById(R.id.card_realtime)
        card_payment = view?.findViewById(R.id.card_payments)
        card_lock = view?.findViewById(R.id.card_lock)
        card_exit = view?.findViewById(R.id.card_exit)
        logout = view.findViewById(R.id.logoutbutton)
        logout?.setOnClickListener(View.OnClickListener {
            DashboardActivityListener!!.logout()
        })

        card_vehicles?.setOnClickListener(View.OnClickListener {
            DashboardActivityListener!!.open_vehicle()
        })

        card_realtime?.setOnClickListener(View.OnClickListener {
            DashboardActivityListener!!.open_realtime()
        })

        card_payment?.setOnClickListener(View.OnClickListener {
            DashboardActivityListener!!.open_payments()
        })

        card_lock?.setOnClickListener(View.OnClickListener {
            DashboardActivityListener!!.open_lock()
        })

        card_exit?.setOnClickListener(View.OnClickListener {
            DashboardActivityListener!!.exit()
        })

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = context as Activity
        DashboardActivityListener = activity as onDashboardActivityListner
    }

}
