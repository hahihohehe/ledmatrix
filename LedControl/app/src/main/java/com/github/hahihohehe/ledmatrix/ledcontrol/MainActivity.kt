package com.github.hahihohehe.ledmatrix.ledcontrol

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.security.NetworkSecurityPolicy
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var pager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setSupportActionBar(findViewById(R.id.toolbar))

        checkPermissions()

        tabLayout = findViewById(R.id.tab_layout)
        pager = findViewById(R.id.pager)
        pager.adapter = MatricesAdapter(this)
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = position.toString()
        }.attach()

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, MainFragment.newInstance())
//                .commitNow()
//        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED) {
            println("permission not granted")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 1);
        }
        else {
            println("permission granted")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted)
                Toast.makeText(this, "HTTP erlaubt", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this, "nur HTTPS erlaubt", Toast.LENGTH_LONG).show()
        }
    }
}