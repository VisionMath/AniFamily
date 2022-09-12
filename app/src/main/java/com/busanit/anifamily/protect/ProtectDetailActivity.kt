package com.busanit.anifamily.protect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.busanit.anifamily.Geocode
import com.busanit.anifamily.MyApplication
import com.busanit.anifamily.databinding.ActivityProtectDetailBinding
import com.busanit.anifamily.protect.model.Item
import com.busanit.anifamily.protect.model.ReplyList
import com.busanit.anifamily.protect.model.ReplyModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProtectDetailActivity : AppCompatActivity() {

    private var curLat: Double = 0.0
    private var curLon: Double = 0.0
    lateinit var binding: ActivityProtectDetailBinding
    lateinit var protect: Item

    init {
        instance = this
    }

    companion object {
        private var instance: ProtectDetailActivity? = null
        fun getInstance(): ProtectDetailActivity? {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProtectDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        protect = intent.getSerializableExtra("protect") as Item

        GlobalScope.launch {
            Geocode.requestGeocode(protect?.careAddr.toString())!!
            withContext(Dispatchers.Main) {
                while (binding.mapX.text.isNullOrBlank()) {
                    delay(100)
                }
            }
            curLat = binding.mapY.text.toString().toDoubleOrNull()!!
            curLon = binding.mapX.text.toString().toDoubleOrNull()!!

        }

        Glide.with(this).load(protect?.popfile).into(binding.popfile)
        binding.kindCd.text = protect?.kindCd

        val info =
            if (protect?.sexCd == "F") "암컷/" else "수컷/" + protect?.colorCd + "/" + protect?.age + "/" + protect?.weight
        binding.info.text = info

        binding.noticeNo.text = protect?.noticeNo
        val noticeDay = protect?.noticeSdt?.let {
            it.substring(0, 4) + "-" + it.substring(4, 6) + "-" + it.substring(6, 8)
        } + "~" + protect?.noticeEdt?.let {
            it.substring(0, 4) + "-" + it.substring(4, 6) + "-" + it.substring(6, 8)
        }
        binding.notice.text = protect.processState
        binding.noticeDay.text = noticeDay
        binding.happenPlace.text = protect?.happenPlace
        binding.specialMark.text = protect?.specialMark
        binding.shelter.text = protect?.let { it.careNm + " (" + it.careTel + ")" }
        binding.office.text = protect?.let { it.orgNm + " (" + it.officetel + ")" }
        binding.center.text = protect?.careNm

        binding.popfile.setOnClickListener {
            val intent = Intent(this, PopImageActivity::class.java)
            intent.putExtra("popfile", protect?.popfile)
            startActivity(intent)
        }

        binding.goCenter.setOnClickListener {
            Log.d("Rog", "$curLat")

            val intent = Intent(this, CenterActivity::class.java)
            intent.putExtra("careAddr", protect?.careAddr)
            intent.putExtra("careNm", protect?.careNm)
            intent.putExtra("careTel", protect?.careTel)
            intent.putExtra("curLat", curLat)
            intent.putExtra("curLon", curLon)
            startActivity(intent)
        }

        fun getReply() {
            val networkService = ReplyApplication.networkService
            val replyListCall = networkService.doGetList(protect?.desertionNo.toString())
            replyListCall.enqueue(object : Callback<ReplyList> {
                override fun onResponse(
                    call: Call<ReplyList>,
                    response: Response<ReplyList>,
                ) {
                    if (response.isSuccessful) {
                        binding.replayRecycler.layoutManager =
                            LinearLayoutManager(this@ProtectDetailActivity)
                        binding.replayRecycler.adapter =
                            ReplyAdapter(this@ProtectDetailActivity, response.body()?.replys)
                        binding.replayRecycler.addItemDecoration(
                            DividerItemDecoration(this@ProtectDetailActivity,
                                LinearLayoutManager.VERTICAL))
                        binding.replayRecycler.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<ReplyList>, t: Throwable) {
                    Log.d("Rog", "${t.message}")
                    call.cancel()
                }
            })
        }

        getReply()

        binding.replyButton.setOnClickListener {

            if (MyApplication.checkAuth() or !MyApplication.email.isNullOrBlank()) {
                if (!binding.replayInput.text.isNullOrBlank()) {
                    var replyModel = MyApplication.email?.let { it1 ->
                        protect?.desertionNo?.let { it2 ->
                            ReplyModel(
                                id = 0,
                                content = binding.replayInput.text.toString(),
                                email = it1,
                                desertion = it2
                            )
                        }
                    }

                    val networkService = ReplyApplication.networkService
                    val replyInsertCall = networkService.doInsert(replyModel!!)
                    replyInsertCall?.enqueue(object : Callback<String> {
                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>,
                        ) {
                            Log.d("Rog", "insert 성공")
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.d("Rog", "${t.message}")
                            call.cancel()
                        }
                    })
                } else {
                    Toast.makeText(this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                }
//                finish()
//                startActivity(intent)
                getReply()

            } else {
                Toast.makeText(this, "먼저 로그인하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            Geocode.requestGeocode(protect?.careAddr.toString())!!
            withContext(Dispatchers.Main) {
                while (binding.mapX.text.isNullOrBlank()) {
                    delay(100)
                }
            }
            curLat = binding.mapY.text.toString().toDoubleOrNull()!!
            curLon = binding.mapX.text.toString().toDoubleOrNull()!!

        }
    }
}