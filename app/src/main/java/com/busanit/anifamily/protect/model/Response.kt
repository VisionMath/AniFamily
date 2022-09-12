package com.vision.android3.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Response (
    @SerializedName("header")
    @Expose
    var header: Header,

    @SerializedName("body")
    @Expose
    var body: Body
)