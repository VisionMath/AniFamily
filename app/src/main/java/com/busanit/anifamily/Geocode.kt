package com.busanit.anifamily

import com.busanit.anifamily.protect.CenterActivity
import com.busanit.anifamily.protect.ProtectDetailActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class Geocode {
    companion object {

        private val protectDetailActivity = ProtectDetailActivity.getInstance()

        fun requestGeocode(addr:String) {
            try {
                val bufferedReader: BufferedReader
                val stringBuilder = StringBuilder()
//                val addr = "부산광역시 해운대구 송정2로13번길 46 (송정동)"
                val query =
                    "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(
                        addr,
                        "UTF-8")
                val url = URL(query)
                val conn = url.openConnection() as HttpURLConnection
                if (conn != null) {
                    conn.connectTimeout = 5000
                    conn.readTimeout = 5000
                    conn.requestMethod = "GET"
                    conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "qmyqn31ysl")
                    conn.setRequestProperty("X-NCP-APIGW-API-KEY",
                        "wko2b07hVBme714URyP7yqtFm6juQDKFcP2PACjs")
                    conn.doInput = true
                    val responseCode = conn.responseCode
                    bufferedReader = if (responseCode == 200) {
                        BufferedReader(InputStreamReader(conn.inputStream))
                    } else {
                        BufferedReader(InputStreamReader(conn.errorStream))
                    }
                    var line: String?
                    while (bufferedReader.readLine().also { line = it } != null) {
                        stringBuilder.append("$line".trimIndent())
                    }
                    var indexFirst: Int = stringBuilder.indexOf("\"x\":\"")
                    var indexLast: Int = stringBuilder.indexOf("\",\"y\":")
                    val x = stringBuilder.substring(indexFirst + 5, indexLast)
                    indexFirst = stringBuilder.indexOf("\"y\":\"")
                    indexLast = stringBuilder.indexOf("\",\"distance\":")
                    val y = stringBuilder.substring(indexFirst + 5, indexLast)

                    protectDetailActivity?.binding!!.mapX!!.text = x
                    protectDetailActivity.binding!!.mapY!!.text = y

                    bufferedReader.close()
                    conn.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}