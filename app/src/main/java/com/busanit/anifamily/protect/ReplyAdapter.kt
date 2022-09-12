package com.busanit.anifamily.protect

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busanit.anifamily.databinding.ReplyItemBinding
import com.busanit.anifamily.protect.model.ReplyModel

class ReplyViewHolder(val binding: ReplyItemBinding) : RecyclerView.ViewHolder(binding.root)

class ReplyAdapter(var context: Context, private val datas: List<ReplyModel>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder = ReplyViewHolder(
        ReplyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val binding = (holder as ReplyViewHolder).binding
        val reply = datas?.get(position)

        binding.replyContent.text = reply?.content
        binding.replyEmail.text = reply?.email
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }
}