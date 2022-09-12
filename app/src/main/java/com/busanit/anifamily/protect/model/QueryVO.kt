package com.busanit.anifamily.protect.model

import java.io.Serializable

data class QueryVO(
    var fromDay:String,
    var toDay:String,
    var recent:Boolean,
    var city:String,
    var district:String,
    var kind:String,
    var protecting:Boolean,
    var alertResque:Boolean
):Serializable
