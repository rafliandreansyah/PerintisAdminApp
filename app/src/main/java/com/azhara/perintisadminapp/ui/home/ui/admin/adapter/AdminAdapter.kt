package com.azhara.perintisadminapp.ui.home.ui.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.databinding.ItemAdminBinding
import com.azhara.perintisadminapp.databinding.ItemUserBinding
import com.azhara.perintisadminapp.entity.AdminData
import com.azhara.perintisadminapp.entity.UserData
import com.bumptech.glide.Glide

class AdminAdapter : ListAdapter<AdminData, AdminAdapter.AdminViewHolder>(DIFF_UTIL){

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<AdminData>(){
            override fun areItemsTheSame(oldItem: AdminData, newItem: AdminData): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: AdminData, newItem: AdminData): Boolean =
                    oldItem.email == newItem.email

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAdapter.AdminViewHolder =
            AdminViewHolder(ItemAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: AdminAdapter.AdminViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AdminViewHolder(val binding: ItemAdminBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(adminData: AdminData){
            with(binding){
                tvItemAdminEmail.text = adminData.email
                tvItemAdminName.text = adminData.name
                if (adminData.imgUrl != null && adminData.imgUrl != ""){
                    Glide.with(itemView).load(adminData.imgUrl).into(imgItemAdmin)
                }
            }
        }
    }

}