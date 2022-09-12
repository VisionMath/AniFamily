package com.busanit.anifamily.shelter

import android.app.Application
import com.busanit.anifamily.home.HomeNetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShelterApplication:Application() {
    companion object {
        var networkService: ShelterNetworkService
        private val retrofit:Retrofit
            get()=Retrofit.Builder()
                .baseUrl("http://172.28.64.1:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        init{
            networkService=retrofit.create(ShelterNetworkService::class.java)
        }
    }
}