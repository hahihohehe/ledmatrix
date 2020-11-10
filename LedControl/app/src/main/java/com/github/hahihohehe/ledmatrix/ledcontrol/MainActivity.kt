package com.github.hahihohehe.ledmatrix.ledcontrol

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.security.NetworkSecurityPolicy
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.hahihohehe.ledmatrix.ledcontrol.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        checkPermissions()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
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