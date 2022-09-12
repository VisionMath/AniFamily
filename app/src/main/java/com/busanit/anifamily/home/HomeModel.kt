package com.busanit.anifamily.home

import java.io.Serializable

data class HomeModel(
    var id:Long,
    var title:String,
    var content:String,
    var link:String,
):Serializable
