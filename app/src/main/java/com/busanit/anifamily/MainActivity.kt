package com.busanit.anifamily

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.busanit.anifamily.databinding.ActivityMainBinding
import com.busanit.anifamily.home.HomeFragment
import com.busanit.anifamily.protect.ProtectFragment
import com.busanit.anifamily.shelter.ShelterFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.navercorp.nid.NaverIdLoginSDK
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null
        fun getInstance(): MainActivity? {
            return instance
        }
    }

//    lateinit var auth : FirebaseAuth

    private var curLat: Double = 0.0
    private var curLon: Double = 0.0
    private var address: List<String> = listOf("대한민국", "서울특별시", "중구", "명동")
    lateinit var binding: ActivityMainBinding
    private var menu: Menu? = null
//    lateinit var point:Point

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        class MyFragmentPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
            private val fragments: List<Fragment> =
                listOf(HomeFragment(),
                    LostFragment(),
                    ProtectFragment(),
                    ShelterFragment(),
                    StoryFragment())

            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }

        val tabTitles = listOf("홈", "실종", "보호", "보호소찾기", "스토리")
        val adapter = MyFragmentPagerAdapter(this)
        binding.viewpager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
        }

        var geocoder = Geocoder(this, Locale.KOREA)

        GlobalScope.launch(Dispatchers.Main) {

            locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER,
                null,
                mainExecutor) { location ->
                curLat = location!!.latitude
                curLon = location!!.longitude
            }
            withContext(Dispatchers.Main) {
                while (curLat == 0.0) {
                    delay(100)
                }
            }

            val addrList =
                geocoder.getFromLocation(curLat, curLon, 1)

            for (addr in addrList) {
                address = addr.getAddressLine(0).split(" ")
            }

            binding.currentCity.text = address[1]
            binding.currentDistrict.text = address[2]
        }
    }

    override fun onStart() {
        super.onStart()
        if (this.menu != null) {
            if (MyApplication.checkAuth() or !MyApplication.nickname.isNullOrBlank()) {
                menu!!.getItem(0).setIcon(R.drawable.logon)
            } else {
                menu!!.getItem(0).setIcon(R.drawable.login)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        menu.getItem(0).setIcon(R.drawable.logon)
        if (MyApplication.checkAuth() or !MyApplication.nickname.isNullOrBlank()) {
            menu.getItem(0).setIcon(R.drawable.logon)
        } else {
            menu.getItem(0).setIcon(R.drawable.login)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, AuthActivity::class.java))
        return super.onOptionsItemSelected(item)
    }
}

