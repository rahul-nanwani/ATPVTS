package com.example.login.configuration

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.login.fragments.PaidFragment
import com.example.login.fragments.PendingFragment

class TabAdapter(private val myContext: Context, fm: FragmentManager?, var totalTabs: Int) : FragmentPagerAdapter(fm!!) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                PendingFragment()
            }
            1 -> {
                PaidFragment()
            }
            else -> null
        }!!
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

}