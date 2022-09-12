package com.busanit.anifamily.protect

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.busanit.anifamily.District
import com.busanit.anifamily.MainActivity
import com.busanit.anifamily.R
import com.busanit.anifamily.databinding.FragmentProtectBinding
import com.busanit.anifamily.protect.model.QueryVO
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProtectFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var binding: FragmentProtectBinding

    private var city = "1234"
    private var district = "1111"

    private lateinit var cities: Array<String>
    var queryVO: QueryVO = QueryVO("", "", true, city, "", "", false, alertResque = false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProtectBinding.inflate(inflater, container, false)

        val kind = arrayOf("전체", "개", "고양이", "기타")
        val kindCd=arrayOf("417000", "422400", "429900")

        cities = mainActivity.resources!!.getStringArray(R.array.city)
        city = mainActivity.findViewById<TextView>(R.id.currentCity).text.toString()
        district = mainActivity.findViewById<TextView>(R.id.currentDistrict).text.toString()

        val cityCode = mainActivity.findViewById<TextView>(R.id.cityCode)
        val districtCode = mainActivity.findViewById<TextView>(R.id.districtCode)

        fun queryProtect(queryVO: QueryVO) {
            GlobalScope.launch(Dispatchers.Main) {

                var bgnde = queryVO.fromDay.replace("-", "")
                var endde = queryVO.toDay.replace("-", "")
                var upkind=""
                cityCode.text=""

                District.getCode(queryVO.city, queryVO.district, cityCode, districtCode)
                withContext(Dispatchers.IO) {
                    while (cityCode.text == "")
                        delay(100)
                }
                if (queryVO.kind!="전체") {
                    val idxKind = kind.indexOf(queryVO.kind)
                    upkind= kindCd[idxKind-1]
                }

                val networkService = ProtectApplication.networkService
                val protectListCall = networkService.getList(ProtectApplication.API_KEY,
                    "10", bgnde, endde, upkind,
                    cityCode.text.toString(),
                    districtCode.text.toString(),
                    "json")
                protectListCall.enqueue(object : Callback<ProtectList> {
                    override fun onResponse(
                        call: Call<ProtectList>,
                        response: Response<ProtectList>,
                    ) {
                        if (response.isSuccessful) {
                            val itemList = response.body()?.response?.body?.items?.item

                            binding.protectRecyclerView.layoutManager =
                                LinearLayoutManager(activity)
                            binding.protectRecyclerView.adapter =
                                activity?.let { ProtectAdapter(it, itemList) }
                            binding.protectRecyclerView.addItemDecoration(
                                DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))

                            val orgNm = "${queryVO.city} ${queryVO.district}"
                            binding.orgNm.text = orgNm
                            binding.kind.text = queryVO.kind
                        } else {
                            Log.d("Rog", "실패")
                        }
                    }

                    override fun onFailure(call: Call<ProtectList>, t: Throwable) {
                        Log.d("Rog", "${t.message}")
                        call.cancel()
                    }
                })
            }
        }

        queryVO.city = city
        queryVO.district = district
        queryVO.kind= kind[0]
        queryProtect(queryVO)

        binding.queryFragment.setOnClickListener {
            val dlg = QueryDialogue(mainActivity)
            dlg.show(queryVO)

            dlg.setQueryClickedListener { queryVO ->
                queryProtect(queryVO)
            }
        }
        return binding.root
    }
}