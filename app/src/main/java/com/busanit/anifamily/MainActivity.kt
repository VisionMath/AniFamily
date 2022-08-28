package com.busanit.anifamily

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.busanit.anifamily.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        class MyFragmentPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
            private val fragments: List<Fragment> =
                listOf(HomeFragment(), LostFragment(), ProtectFragment(), ShelterFragment(), StoryFragment())

            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }

        val tabTitles = listOf("홈", "실종", "보호", "보호소찾기", "스토리")
        val adapter = MyFragmentPagerAdapter(this)
        binding.viewpager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}