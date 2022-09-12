package com.busanit.anifamily.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.busanit.anifamily.databinding.HomeItemBinding

class HomeViewHolder(val binding: HomeItemBinding) : RecyclerView.ViewHolder(binding.root)

class HomeAdapter(var context: Context, private val datas: List<HomeModel>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder = HomeViewHolder(
        HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val binding = (holder as HomeViewHolder).binding
        val home = datas?.get(position)
        val intent = Intent(this.context, HomeDetailActivity::class.java)

        binding.title.text = home?.title
        Glide.with(context).load(home?.link).into(binding.image)

        binding.root.setOnClickListener {
            intent.putExtra("home", home)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }
}