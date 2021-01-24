package com.azhara.perintisadminapp.ui.home.ui.mitraregister.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.databinding.ItemMitraRegisterBinding
import com.azhara.perintisadminapp.entity.MitraRegisterData

class MitraRegisterAdapter : ListAdapter<MitraRegisterData, MitraRegisterAdapter.MitraRegisterViewHolder>(DIFF_UTIL){

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<MitraRegisterData>(){
            override fun areItemsTheSame(oldItem: MitraRegisterData, newItem: MitraRegisterData): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: MitraRegisterData, newItem: MitraRegisterData): Boolean =
                    oldItem.email == newItem.email

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MitraRegisterAdapter.MitraRegisterViewHolder =
            MitraRegisterViewHolder(ItemMitraRegisterBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MitraRegisterAdapter.MitraRegisterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MitraRegisterViewHolder(val binding: ItemMitraRegisterBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: MitraRegisterData){
            with(binding){
                if (data.carTransmision == 1){
                    tvItemMitraRegisterCarTransmision.text = "Automatic"
                }else{
                    tvItemMitraRegisterCarTransmision.text = "Manual"
                }

                tvItemMitraRegisterCarType.text = data.carType
                tvItemMitraRegisterCarYear.text = "${data.carYear}"
                tvItemMitraRegisterEmail.text = data.email
                tvItemMitraRegisterPhoneNumber.text = "${data.phoneNumber}"
                tvItemMitraRegisterStatus.text = "Menunggu Konfirmasi"
                tvItemMitraRegisterName.text = data.name
            }
        }
    }

}