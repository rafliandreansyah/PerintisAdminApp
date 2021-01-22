package com.azhara.perintisadminapp.ui.home.ui.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.databinding.ItemUserBinding
import com.azhara.perintisadminapp.entity.UserData
import com.bumptech.glide.Glide

class UserAdapter : ListAdapter<UserData, UserAdapter.UserViewHolder>(DIFF_UTIL){

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<UserData>(){
            override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean =
                    oldItem.email == newItem.email

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder =
            UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(userData: UserData){
            with(binding){
                tvItemUserEmail.text = userData.email
                tvItemUserName.text = userData.name
                if (userData.imgUrl != null && userData.imgUrl != ""){
                    Glide.with(itemView).load(userData.imgUrl).into(imgItemUser)
                }
            }
        }
    }

}