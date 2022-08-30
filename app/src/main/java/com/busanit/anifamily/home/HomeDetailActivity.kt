package com.busanit.anifamily.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.busanit.anifamily.MyApplication
import com.busanit.anifamily.databinding.ActivityHomeDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityHomeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id=intent.getLongExtra("id",0)
        val networkService= MyApplication.networkService
        val userModelCall=networkService.doGetUser(id)
        userModelCall.enqueue(object:Callback<UserModel>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>
            ) {
                val userModel=response.body()
                binding.tvUser.text="""
                    ${userModel?.id},
                    ${userModel?.name},
                    ${userModel?.username},
                    ${userModel?.tel}
                """
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                call.cancel()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}