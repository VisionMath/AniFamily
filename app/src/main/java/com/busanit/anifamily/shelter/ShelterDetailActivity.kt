package com.busanit.anifamily.shelter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.busanit.anifamily.databinding.ActivityHomeDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShelterDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityHomeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        val id=intent.getStringExtra("id")
//        val networkService= ShelterApplication.networkService
//        val shelterListCall=networkService.doGetShelter(id)
//        shelterListCall.enqueue(object:Callback<ShelterList>{
//            @SuppressLint("SetTextI18n")
//            override fun onResponse(call: Call<ShelterList>, response: Response<ShelterList>) {
//                val shelterModel=response.body()
//                binding.tvUser.text="""
//                    ${shelterModel?.careRegNo},
//                    ${shelterModel?.careNm},
//                """
//            }
//
//            override fun onFailure(call: Call<ShelterModel>, t: Throwable) {
//                call.cancel()
//            }
//        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}