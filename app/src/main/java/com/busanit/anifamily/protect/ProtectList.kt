package com.busanit.anifamily.protect

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vision.android3.model.Response

data class ProtectList(
    @SerializedName("response")
    @Expose
    var response: Response
)
