package com.vision.android3.model

import com.busanit.anifamily.protect.model.Item
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Items (
    @SerializedName("item")
    @Expose
    var item: List<Item>
)