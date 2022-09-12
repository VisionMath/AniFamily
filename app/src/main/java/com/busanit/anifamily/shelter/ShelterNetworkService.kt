package com.busanit.anifamily.shelter

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShelterNetworkService {
    @GET("shelter/list")
    fun doGetShelterList(): Call<ShelterList>

    @GET("shelter/getShelter")
    fun doGetShelter(@Query("uprNm") uprNm: String?=null):Call<ShelterList>

    @GET("shelter/getDistrict")
    fun doGetDistrict(@Query("uprNm") uprNm:String?=null):Call<DistrictList>

    @GET("shelter/getDistrictAll")
    fun doGetDistrictAll():Call<DistrictList>

    @GET("shelter/getCityCode")
    fun getCityCode(city: String):Call<DistrictModel>
}