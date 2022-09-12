package com.busanit.anifamily.protect

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReplyApplication : Application() {
    companion object {
        var networkService: ReplyNetworkService
        private val retrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl("http://172.28.64.1:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        init {
            networkService = retrofit.create(ReplyNetworkService::class.java)
        }
    }
}