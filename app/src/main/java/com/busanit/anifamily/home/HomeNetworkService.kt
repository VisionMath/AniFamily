package com.busanit.anifamily.home

import retrofit2.Call
import retrofit2.http.*

interface HomeNetworkService {

    @GET("home/list")
    fun doGetHomeList(): Call<HomeList>

    @GET("home/getHome")
    fun doGetHome(@Query("id") id:Long): Call<HomeModel>
}

