package com.busanit.anifamily.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.busanit.anifamily.databinding.ActivityHomeInputBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityHomeInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSave.setOnClickListener {
            var userModel= UserModel(
                id=0,
                username=binding.edUserName.text.toString(),
                name=binding.editName.text.toString(),
                password = binding.edPassword.text.toString(),
                tel = binding.edTel.text.toString(),
                roles="USER_ROLE"
            )
//            val networkService= HomeApplication.networkService
//            val userInsertCall=networkService.insert(userModel)
//            userInsertCall.enqueue(object:Callback<String>{
//                override fun onResponse(
//                    call: Call<String>,
//                    response: Response<String>
//                ) {
//                    Log.d("pgm",response.body().toString())
//                }
//
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    call.cancel()
//                }
//            })
//            finish()
        }
    }
}