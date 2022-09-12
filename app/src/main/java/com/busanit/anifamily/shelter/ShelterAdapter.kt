package com.busanit.anifamily.shelter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busanit.anifamily.databinding.ShelterItemBinding

class ShelterViewHolder(val binding: ShelterItemBinding):RecyclerView.ViewHolder(binding.root)

class ShelterAdapter(var context: Context, private val datas:List<ShelterModel>?):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder= ShelterViewHolder(
        ShelterItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val binding=(holder as ShelterViewHolder).binding
        val shelter=datas?.get(position)
        val intent= Intent(this.context, ShelterDetailActivity::class.java)

        binding.careNm.text=shelter?.careNm
        binding.orgNm.text=shelter?.orgNm
//        binding.root.setOnClickListener {
//
//            intent.putExtra("id",shelter?.careRegNo)
//            context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return datas?.size ?:0
    }
}