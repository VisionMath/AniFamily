package com.busanit.anifamily.shelter

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.busanit.anifamily.MainActivity
import com.busanit.anifamily.R
import com.busanit.anifamily.databinding.ActivityMainBinding
import com.busanit.anifamily.databinding.FragmentShelterBinding
import com.busanit.anifamily.home.HomeAdapter
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.function.Consumer

class ShelterFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    private lateinit var binding: FragmentShelterBinding

    private var city = "1234"
    private lateinit var cities: Array<String>

    var datas: List<DistrictModel>? = null
    private var districts: List<String> = mutableListOf("전체")
    //    private var mapView: MapView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): LinearLayout {
        binding = FragmentShelterBinding.inflate(inflater, container, false)

        cities = mainActivity.resources!!.getStringArray(R.array.city)
        city = mainActivity.findViewById<TextView>(R.id.currentCity).text.toString()

        val idx = cities.indexOf(city)
        val cityAdapter =
            ArrayAdapter(mainActivity, android.R.layout.simple_spinner_item, cities)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.city.adapter = cityAdapter
        binding.city.setSelection(idx)

        val networkService = ShelterApplication.networkService
        val districtListCall = networkService.doGetDistrict(city)

        districtListCall.enqueue(object : Callback<DistrictList> {
            override fun onResponse(
                call: Call<DistrictList>,
                response: Response<DistrictList>,
            ) {
                if (response.isSuccessful) {
                    datas = response.body()?.district!!
                }
                for (i in 0 until datas?.size!!) {
                    districts += datas!![i].orgNm
                }

                val districtAdapter =
                    ArrayAdapter(mainActivity, android.R.layout.simple_spinner_item, districts)
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.district.adapter = districtAdapter
            }

            override fun onFailure(call: Call<DistrictList>, t: Throwable) {
            }
        })

        val shelterListCall = networkService.doGetShelter(city)

        shelterListCall.enqueue(object : Callback<ShelterList> {

            override fun onResponse(call: Call<ShelterList>, response: Response<ShelterList>) {
                if (response.isSuccessful) {
                    binding.shelterRecyclerView.layoutManager = LinearLayoutManager(activity)
                    binding.shelterRecyclerView.adapter =
                        activity?.let { ShelterAdapter(it, response.body()?.shelter) }
                    binding.shelterRecyclerView.addItemDecoration(
                        DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
                }
            }

            override fun onFailure(call: Call<ShelterList>, t: Throwable) {
                call.cancel()
            }
        })
        return binding.root
    }
}

