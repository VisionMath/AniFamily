package com.vision.android3.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Body (
    @SerializedName("items")
    @Expose
    var items: Items,

    @SerializedName("numOfRows")
    @Expose
    var numOfRows: Int,

    @SerializedName("pageNo")
    @Expose
    var pageNo: Int,

    @SerializedName("totalCount")
    @Expose
    var totalCount: Int
)