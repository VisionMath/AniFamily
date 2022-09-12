package com.busanit.anifamily.protect.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Item (
    @SerializedName("desertionNo")
    @Expose
    var desertionNo: String? = null,

    @SerializedName("filename")
    @Expose
    var filename: String? = null,

    @SerializedName("happenDt")
    @Expose
    var happenDt: String? = null,

    @SerializedName("happenPlace")
    @Expose
    var happenPlace: String? = null,

    @SerializedName("kindCd")
    @Expose
    var kindCd: String? = null,

    @SerializedName("colorCd")
    @Expose
    var colorCd: String? = null,

    @SerializedName("age")
    @Expose
    var age: String? = null,

    @SerializedName("weight")
    @Expose
    var weight: String? = null,

    @SerializedName("noticeNo")
    @Expose
    var noticeNo: String? = null,

    @SerializedName("noticeSdt")
    @Expose
    var noticeSdt: String? = null,

    @SerializedName("noticeEdt")
    @Expose
    var noticeEdt: String? = null,

    @SerializedName("popfile")
    @Expose
    var popfile: String? = null,

    @SerializedName("processState")
    @Expose
    var processState: String? = null,

    @SerializedName("sexCd")
    @Expose
    var sexCd: String? = null,

    @SerializedName("neuterYn")
    @Expose
    var neuterYn: String? = null,

    @SerializedName("specialMark")
    @Expose
    var specialMark: String? = null,

    @SerializedName("careNm")
    @Expose
    var careNm: String? = null,

    @SerializedName("careTel")
    @Expose
    var careTel: String? = null,

    @SerializedName("careAddr")
    @Expose
    var careAddr: String? = null,

    @SerializedName("orgNm")
    @Expose
    var orgNm: String? = null,

    @SerializedName("chargeNm")
    @Expose
    var chargeNm: String? = null,

    @SerializedName("officetel")
    @Expose
    var officetel: String? = null,
):Serializable