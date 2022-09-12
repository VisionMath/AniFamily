package com.busanit.anifamily.protect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.busanit.anifamily.R
import com.busanit.anifamily.databinding.ActivityPopImageBinding
import kotlinx.coroutines.GlobalScope

class PopImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityPopImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val popfile=intent.getStringExtra("popfile")
        Glide.with(this).load(popfile).into(binding.largeImage)

    }
}