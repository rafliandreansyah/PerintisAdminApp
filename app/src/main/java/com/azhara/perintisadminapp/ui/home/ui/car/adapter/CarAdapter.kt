package com.azhara.perintisadminapp.ui.home.ui.car.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
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
    private var onMenuClickListener: OnMenuClickListener? = null

    fun setOnMenuItemClicked(onMenuClickListener: OnMenuClickListener?){
        this.onMenuClickListener = onMenuClickListener
    }

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
                tvItemCarYear.text = "${carData.year}"

                if (carData.statusReady == true){
                    tvItemCarStatusEnabled.text = "Enable"
                    tvItemCarStatusEnabled.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreen))
                }else{
                    tvItemCarStatusEnabled.text = "Disable"
                    tvItemCarStatusEnabled.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorRed))
                    tvItemCarName.setTextColor(ContextCompat.getColor(itemView.context, R.color.common_google_signin_btn_text_dark_disabled))
                    tvItemCarOwner.setTextColor(ContextCompat.getColor(itemView.context, R.color.common_google_signin_btn_text_dark_disabled))
                    tvItemCarPrice.setTextColor(ContextCompat.getColor(itemView.context, R.color.common_google_signin_btn_text_dark_disabled))
                    tvItemCarYear.setTextColor(ContextCompat.getColor(itemView.context, R.color.common_google_signin_btn_text_dark_disabled))
                    tvOwnerTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.common_google_signin_btn_text_dark_disabled))
                    tvYearTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.common_google_signin_btn_text_dark_disabled))
                    tvPriceTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.common_google_signin_btn_text_dark_disabled))
                }

                itemCar.setOnClickListener {
                    onItemClickListener?.onItemClicked(carData)
                }

                btnOptionCar.setOnClickListener {
                    val popupMenu = PopupMenu(itemView.context, btnOptionCar)
                    popupMenu.inflate(R.menu.menu_list_car)
                    if (carData.statusReady == false){
                        popupMenu.menu.getItem(0).title = "Aktifkan"
                    }else{
                        popupMenu.menu.getItem(0).title = "Non-Aktifkan"
                    }
                    popupMenu.setOnMenuItemClickListener {
                        when(it.itemId){
                            R.id.optionEditStatusActive -> {
                                onMenuClickListener?.onMenuClicked(carData, "edit")
                            }
                            R.id.optionDelete -> {
                                onMenuClickListener?.onMenuClicked(carData, "delete")
                            }
                        }

                        true
                    }
                    popupMenu.show()
                }

            }
        }
    }

}

interface OnMenuClickListener{
    fun onMenuClicked(carData: CarsData, typeAction: String?)
}

interface OnItemClickListener{
    fun onItemClicked(carData: CarsData)
}