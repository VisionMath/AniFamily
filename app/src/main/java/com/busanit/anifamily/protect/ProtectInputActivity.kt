package com.busanit.anifamily.protect

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.busanit.anifamily.databinding.ActivityProtectInputBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProtectInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityProtectInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSave.setOnClickListener {
            var protectModel= ProtectModel(
                id=0,
                protectname=binding.edUserName.text.toString(),
                name=binding.editName.text.toString(),
                password = binding.edPassword.text.toString(),
                tel = binding.edTel.text.toString(),
                roles="USER_ROLE"
            )
            val networkService= ProtectApplication.networkService
//            val protectInsertCall=networkService.insert(protectModel)
//            protectInsertCall.enqueue(object:Callback<String>{
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