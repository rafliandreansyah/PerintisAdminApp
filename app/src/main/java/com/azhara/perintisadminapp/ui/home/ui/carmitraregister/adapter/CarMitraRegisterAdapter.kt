package com.azhara.perintisadminapp.ui.home.ui.carmitraregister.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.databinding.ItemCarRegisterMitraBinding
import com.azhara.perintisadminapp.entity.CarMitraRegisterData

class CarMitraRegisterAdapter: ListAdapter<CarMitraRegisterData, CarMitraRegisterAdapter.CarMitraRegisterViewHolder>(DIFF_UTIL) {

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<CarMitraRegisterData>(){
            override fun areItemsTheSame(oldItem: CarMitraRegisterData, newItem: CarMitraRegisterData): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: CarMitraRegisterData, newItem: CarMitraRegisterData): Boolean =
                    oldItem.carType == newItem.carType

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarMitraRegisterAdapter.CarMitraRegisterViewHolder =
            CarMitraRegisterViewHolder(ItemCarRegisterMitraBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CarMitraRegisterAdapter.CarMitraRegisterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CarMitraRegisterViewHolder(val binding: ItemCarRegisterMitraBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: CarMitraRegisterData){
            with(binding){
                tvItemCarMitraRegisterCarName.text = data.carType
                tvItemCarMitraRegisterCarYear.text = "${data.carYear}"
                tvItemCarMitraRegisterStatus.text = "Menunggu Konfirmasi"
                tvItemCarMitraRegisterTransmision.text = data.carTransmission
            }
        }
    }

}