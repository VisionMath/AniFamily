package com.busanit.anifamily

import android.content.Context
import android.util.Log
import android.widget.TextView
import com.busanit.anifamily.shelter.DistrictList
import com.busanit.anifamily.shelter.DistrictModel
import com.busanit.anifamily.shelter.ShelterApplication
import com.opencsv.CSVReader
import kotlinx.coroutines.delay
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStreamReader

class District {
    companion object {

        fun readDistrict(context: Context): MutableList<Array<String>>? {

            val assetManager = context.assets
            val inputStream = assetManager.open("district.csv")
            val reader = CSVReader(InputStreamReader(inputStream))

            return reader.readAll()
        }

        fun getCode(city: String, district:String, cityCode: TextView, districtCode:TextView) {
            var datas: List<DistrictModel>? = null
            val networkService = ShelterApplication.networkService
            val districtListCall = networkService.doGetDistrict(city)

            districtListCall.enqueue(object : Callback<DistrictList> {
                override fun onResponse(
                    call: Call<DistrictList>,
                    response: Response<DistrictList>,
                ) {
                    if (response.isSuccessful) {
                        datas = response.body()?.district!!
                        cityCode.text = datas!![0].uprCd

                        for (i in datas!!) {
                            if (i.orgNm==district) {
                                districtCode.text = i.orgCd
                                break
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<DistrictList>, t: Throwable) {
                }
            })
        }
    }
}