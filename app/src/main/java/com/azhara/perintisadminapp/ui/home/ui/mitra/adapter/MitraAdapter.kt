package com.azhara.perintisadminapp.ui.home.ui.mitra.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.databinding.ItemMitraBinding
import com.azhara.perintisadminapp.databinding.ItemUserBinding
import com.azhara.perintisadminapp.entity.MitraData
import com.azhara.perintisadminapp.entity.UserData
import com.bumptech.glide.Glide

class MitraAdapter : ListAdapter<MitraData, MitraAdapter.MitraViewHolder>(DIFF_UTIL){

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<MitraData>(){
            override fun areItemsTheSame(oldItem: MitraData, newItem: MitraData): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: MitraData, newItem: MitraData): Boolean =
                    oldItem.email == newItem.email

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MitraAdapter.MitraViewHolder =
            MitraViewHolder(ItemMitraBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MitraAdapter.MitraViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MitraViewHolder(val binding: ItemMitraBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(mitraData: MitraData){
            with(binding){
                tvItemMitraEmail.text = mitraData.email
                tvItemMitraName.text = mitraData.owner
                if (mitraData.imgUrl != null && mitraData.imgUrl != ""){
                    Glide.with(itemView).load(mitraData.imgUrl).into(imgItemMitra)
                }
            }
        }
    }

}