package com.busanit.anifamily.protect

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.busanit.anifamily.databinding.ProtectItemBinding
import com.busanit.anifamily.protect.model.Item

class ProtectViewHolder(val binding: ProtectItemBinding) : RecyclerView.ViewHolder(binding.root)

class ProtectAdapter(var context: Context, private val datas: List<Item>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder = ProtectViewHolder(
        ProtectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val binding = (holder as ProtectViewHolder).binding
        val protect = datas?.get(position)
        val intent = Intent(this.context, ProtectDetailActivity::class.java)

        Glide.with(context).load(protect?.filename).into(binding.filename)
        binding.processState.text = protect?.processState
        binding.sexCd.text = protect?.sexCd
        binding.kindCd.text = protect?.kindCd
        var hDate = ""
        protect?.happenDt?.let {
            hDate = it.substring(0, 4) + "-" + it.substring(4, 6) + "-" + it.substring(6, 8)
        }
        binding.happenDt.text = hDate
        binding.orgNm.text = protect?.orgNm
        binding.happenPlace.text = protect?.happenPlace

        binding.root.setOnClickListener {
            intent.putExtra("protect", protect)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }
}