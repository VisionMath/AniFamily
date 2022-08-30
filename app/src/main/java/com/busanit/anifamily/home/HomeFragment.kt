package com.busanit.anifamily.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.busanit.anifamily.MyApplication
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

        val networkService= MyApplication.networkService
        val userListCall=networkService.doGetUserList()
        userListCall.enqueue(object:Callback<UserList>{
            override fun onResponse(
                call: Call<UserList>,
                response: Response<UserList>
            ) {
                if(response.isSuccessful){
                    binding.homeRecyclerView.layoutManager=LinearLayoutManager(activity)
                    binding.homeRecyclerView.adapter= activity?.let { MyAdapter(it,response.body()?.users) }
                    binding.homeRecyclerView.addItemDecoration(
                        DividerItemDecoration(activity,LinearLayoutManager.VERTICAL))
                }
            }

            override fun onFailure(call: Call<UserList>, t: Throwable) {
                call.cancel()
            }
        })

        return binding.root
    }
}