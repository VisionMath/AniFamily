package com.busanit.anifamily.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busanit.anifamily.databinding.ItemBinding

class MyViewHolder(val binding: ItemBinding):RecyclerView.ViewHolder(binding.root)

class MyAdapter(var context: Context, private val datas:List<UserModel>?):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder= MyViewHolder(
        ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val binding=(holder as MyViewHolder).binding
        val user=datas?.get(position)
        val intent= Intent(this.context, HomeDetailActivity::class.java)

        binding.tvUsername.text=user?.username
        binding.tvName.text=user?.name
        binding.root.setOnClickListener {

            intent.putExtra("id",user?.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return datas?.size ?:0
    }
}