package com.busanit.anifamily.home

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeNetworkService {
    @GET("home/list")
    fun doGetUserList(): Call<UserList>

    @GET("home/getUser/{id}")
    fun doGetUser(@Path("id") id:Long):Call<UserModel>

    @POST("home/insert")
    fun insert(@Body user: UserModel):Call<String>
}