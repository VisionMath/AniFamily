package com.busanit.anifamily.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.busanit.anifamily.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val networkService = HomeApplication.networkService
        val homeListCall = networkService.doGetHomeList()
        homeListCall.enqueue(object : Callback<HomeList> {
            override fun onResponse(
                call: Call<HomeList>,
                response: Response<HomeList>,
            ) {
                if (response.isSuccessful) {
                    binding.homeRecyclerView.layoutManager = GridLayoutManager(activity, 2)
                    binding.homeRecyclerView.adapter =
                        activity?.let { HomeAdapter(it, response.body()?.homes) }
                    binding.homeRecyclerView.addItemDecoration(
                        DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
                }
            }

            override fun onFailure(call: Call<HomeList>, t: Throwable) {
                call.cancel()
            }
        })

        return binding.root
    }
}