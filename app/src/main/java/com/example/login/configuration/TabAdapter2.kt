package com.example.login.configuration

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.login.fragments.payBillFragment

class TabAdapter2(private val myContext: Context, fm: FragmentManager?, var totalTabs: Int) : FragmentPagerAdapter(fm!!) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                payBillFragment()
            }
            else -> null
        }!!
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

}