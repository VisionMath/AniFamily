package com.busanit.anifamily.protect

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProtectApplication:Application() {
    companion object {
        private val client: OkHttpClient

        const val API_KEY =
            "dga9YY9sa7uy3rM24lxMLYnA/RvN/OffpF6LBu0GjihbjQv5uOBv7JiqQj188sRINWCeKR2Vw9aHhcSV7/cxXw=="
        private const val BASE_URL = "http://apis.data.go.kr"

        init {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        }

        var networkService: ProtectNetworkService
        private val retrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        init {
            networkService = retrofit.create(ProtectNetworkService::class.java)
        }
    }
}