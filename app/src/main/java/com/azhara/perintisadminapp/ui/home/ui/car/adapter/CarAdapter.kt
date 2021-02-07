package com.azhara.perintisadminapp.ui.home.ui.car.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.ItemCarBinding
import com.azhara.perintisadminapp.entity.CarsData
import com.azhara.perintisadminapp.utils.Helper

class CarAdapter : ListAdapter<CarsData, CarAdapter.CarViewHolder>(DIFF_UTIL){

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<CarsData>(){
            override fun areItemsTheSame(oldItem: CarsData, newItem: CarsData): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: CarsData, newItem: CarsData): Boolean =
                    oldItem.carNumberPlate == newItem.carNumberPlate
        }
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClicked(onItemClickListener: OnItemClickListener?){
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = ItemCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CarViewHolder(val binding: ItemCarBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(carData: CarsData){
            with(binding){
                tvItemCarName.text = carData.carName
                tvItemCarOwner.text = carData.carOwnerName
                tvItemCarPrice.text = "Rp. ${carData.price?.let { Helper.currencyFormat(it) }} /Hari"
                if (carData.statusReady == true){
                    tvItemCarStatusEnabled.text = "Enable"
                    tvItemCarStatusEnabled.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreen))
                }else{
                    tvItemCarStatusEnabled.text = "Disable"
                    tvItemCarStatusEnabled.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorRed))
                }
                tvItemCarYear.text = "${carData.year}"
                itemCar.setOnClickListener {
                    onItemClickListener?.onItemClicked(carData)
                }
            }
        }
    }

}

interface OnItemClickListener{
    fun onItemClicked(carData: CarsData)
}