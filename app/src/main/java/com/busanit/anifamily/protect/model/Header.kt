package com.vision.android3.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Header (
    @SerializedName("resultCode")
    @Expose
    var resultCode: String,

    @SerializedName("resultMsg")
    @Expose
    var resultMsg: String
)