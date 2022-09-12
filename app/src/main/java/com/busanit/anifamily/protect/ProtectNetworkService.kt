package com.busanit.anifamily.protect

import com.busanit.anifamily.protect.model.ReplyList
import com.busanit.anifamily.protect.model.ReplyModel
import retrofit2.Call
import retrofit2.http.*

interface ProtectNetworkService {
    @GET("/1543061/abandonmentPublicSrvc/abandonmentPublic")
    fun getListAll(
        @Query("serviceKey") serviceKey: String?,
        @Query("numOfRows") numOfRows: String?,
        @Query("_type") type: String?,
    ): Call<ProtectList>

    @GET("/1543061/abandonmentPublicSrvc/abandonmentPublic")
    fun getList(
        @Query("serviceKey") serviceKey: String?,
        @Query("numOfRows") numOfRows: String?,
        @Query("bgnde") bgnde: String?,
        @Query("endde") endde: String?,
        @Query("upkind") upkind: String?,
        @Query("upr_cd") uprCd: String?,
        @Query("org_cd") ordCd: String?,
        @Query("_type") type: String?,
    ): Call<ProtectList>

    @POST("reply/insert")
    fun insertReply(@Body reply: ReplyModel): Call<String>

    @GET("reply/getList")
    fun doGetList():Call<ReplyList>

}