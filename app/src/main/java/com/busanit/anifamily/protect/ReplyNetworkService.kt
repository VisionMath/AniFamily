package com.busanit.anifamily.protect

import com.busanit.anifamily.protect.model.ReplyList
import com.busanit.anifamily.protect.model.ReplyModel
import retrofit2.Call
import retrofit2.http.*

interface ReplyNetworkService {

    @GET("reply/list")
    fun doGetList(@Query("desertion") desertion:String): Call<ReplyList>

    @POST("reply/insert")
    fun doInsert(@Body reply: ReplyModel): Call<String>
}

