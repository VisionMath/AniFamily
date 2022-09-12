package com.busanit.anifamily.protect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.busanit.anifamily.Geocode
import com.busanit.anifamily.MainActivity
import com.busanit.anifamily.R
import com.busanit.anifamily.databinding.ActivityCenterBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.*

class CenterActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mapView: MapView? = null
    private var curLat: Double = 0.0
    private var curLon: Double = 0.0

    lateinit var binding: ActivityCenterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val careAddr = intent.getStringExtra("careAddr")
        val careNm = intent.getStringExtra("careNm")
        val careTel = intent.getStringExtra("careTel")
        curLat = intent.getDoubleExtra("curLat", 0.0)
        curLon = intent.getDoubleExtra("curLon", 0.0)

        binding.address.text=careAddr
        binding.careNm.text=careNm
        binding.careTel.text=careTel

        mapView = findViewById<View>(R.id.map_view) as MapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {

        val cameraPosition = CameraPosition(
            LatLng(curLat, curLon), 18.0
        )
        naverMap.cameraPosition = cameraPosition

        val marker = Marker()
        marker.icon = OverlayImage.fromResource(R.drawable.home)
        marker.position = LatLng(curLat, curLon)
        marker.map = naverMap
    }
}