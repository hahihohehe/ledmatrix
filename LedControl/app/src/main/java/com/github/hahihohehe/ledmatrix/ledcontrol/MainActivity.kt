package com.github.hahihohehe.ledmatrix.ledcontrol

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.security.NetworkSecurityPolicy
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.github.hahihohehe.ledmatrix.ledcontrol.ui.main.MatrixFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var pager: ViewPager2
    private lateinit var etIpAddress: EditText
    private lateinit var btnUpload: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setSupportActionBar(findViewById(R.id.toolbar))

        val hasPermissions = checkPermissions()
        tabLayout = findViewById(R.id.tab_layout)
        pager = findViewById(R.id.pager)
        if (hasPermissions)
            initViewPager()

        etIpAddress = findViewById(R.id.etIpAddress)
        btnUpload = findViewById(R.id.btnUpload)
        btnUpload.setOnClickListener {
            if (etIpAddress.text.toString() == "") {
                val behavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet))
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            upload(etIpAddress.text.toString())
        }
    }

    fun initViewPager() {
        pager.adapter = MatricesAdapter(this)
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = position.toString()
        }.attach()
    }

    private fun upload(ipAddress: String) {
        val job = lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = URL("http://$ipAddress/display")

                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                con.requestMethod = "POST"

                con.setRequestProperty("Content-Type", "application/json; utf-8")

                con.doOutput = true

                val jsonInputString = activeFragment.matrixJson

                con.outputStream.use { os ->
                    val input = jsonInputString.toByteArray(charset("utf-8"))
                    os.write(input, 0, input.size)
                }

                val code: Int = con.responseCode
                println(code)

                BufferedReader(InputStreamReader(con.inputStream, "utf-8")).use { br ->
                    val response = StringBuilder()
                    var responseLine: String? = null
                    while (br.readLine().also { responseLine = it } != null) {
                        response.append(responseLine!!.trim { it <= ' ' })
                    }
                    println(response.toString())
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val list = LinkedList<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
        ) {
            println("permission not granted")
            list.add(Manifest.permission.INTERNET)
        } else {
            println("permission granted")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted)
                Toast.makeText(this, "HTTP erlaubt", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this, "nur HTTPS erlaubt", Toast.LENGTH_LONG).show()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            println("permission not granted")
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            println("permission granted")
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            println("permission not granted")
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            println("permission granted")
        }

        if (!list.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                list.toTypedArray(),
                1
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.contains(PackageManager.PERMISSION_DENIED)) {
            Toast.makeText(this, "these permissions are required", Toast.LENGTH_LONG).show()
            finish()
        }
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            if (checkPermissions())
                initViewPager()
            //Handler().postDelayed(this::recreate, 1000)
            //recreate()
//            (pager.adapter as MatricesAdapter).initRepository()
//            TabLayoutMediator(tabLayout, pager) { tab, position ->
//                tab.text = position.toString()
//            }.attach()
        }
    }

    override fun onStart() {
        super.onStart()
        etIpAddress.setText(getPreferences(MODE_PRIVATE).getString(KEY_IP_ADDRESS, ""))
    }

    override fun onStop() {
        super.onStop()
        getPreferences(MODE_PRIVATE).edit()
            .putString(KEY_IP_ADDRESS, etIpAddress.text.toString()).apply()
    }

    var activeFragment = MatrixFragment()

    companion object {
        const val KEY_IP_ADDRESS = "IP_ADDRESS"
    }
}