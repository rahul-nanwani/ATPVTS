package com.example.login.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.login.R
import com.example.login.configuration.TabAdapter
import com.example.login.configuration.TabAdapter2
import com.example.login.fragments.PendingFragment
import com.example.login.fragments.payBillFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener

class PaymentActivity : AppCompatActivity(),PendingFragment.onOpenPendingFragmentListner {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout
        viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        tabLayout.addTab(tabLayout.newTab().setText("Pending"))
        tabLayout.addTab(tabLayout.newTab().setText("Paid"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = TabAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun payBill(billId: String?, vehicleNumber: String?, highwayNumber: String?, totalKm: String?, toll: String?) {
        setContentView(R.layout.activity_payment2)
        supportFragmentManager.beginTransaction().add(R.id.fragment_container_payment, payBillFragment()).commit()
    }
}