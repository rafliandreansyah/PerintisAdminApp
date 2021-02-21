package com.azhara.perintisadminapp.ui.home.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.ItemMitraRegisterDashboardBinding
import com.azhara.perintisadminapp.entity.MitraRegisterData

class MitraRegisterAdapter : ListAdapter<MitraRegisterData, MitraRegisterAdapter.MitraRegisterViewHolder>(DIFF_UTIL){

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<MitraRegisterData>(){
            override fun areItemsTheSame(oldItem: MitraRegisterData, newItem: MitraRegisterData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MitraRegisterData, newItem: MitraRegisterData): Boolean {
                return oldItem.carNumberRegister == newItem.carNumberRegister
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MitraRegisterAdapter.MitraRegisterViewHolder {
        val binding = ItemMitraRegisterDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MitraRegisterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MitraRegisterAdapter.MitraRegisterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MitraRegisterViewHolder(val binding: ItemMitraRegisterDashboardBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dataMitraRegister: MitraRegisterData){
            with(binding){
                tvItemNamePartnerRegister.text = dataMitraRegister.name
                tvItemCarNamePartnerRegister.text = dataMitraRegister.carType
                if (dataMitraRegister.carTransmision == 0){
                    tvItemCarTransmisionPartnerRegister.text = "Manual"
                }else{
                    tvItemCarTransmisionPartnerRegister.text = "Automatic"
                }
                tvItemCarYearPartnerRegister.text = dataMitraRegister.carYear.toString()
                when (dataMitraRegister.statusConfirm) {
                    null -> {
                        tvItemStatusConfirmationPartnerRegister.text = "Menunggu Konfirmasi"
                        tvItemStatusConfirmationPartnerRegister
                            .setTextColor(ContextCompat.getColorStateList(itemView.context, R.color.colorAccent))
                    }
                    1 -> {
                        tvItemStatusConfirmationPartnerRegister.text = "Diterima"
                        tvItemStatusConfirmationPartnerRegister
                            .setTextColor(ContextCompat.getColorStateList(itemView.context, R.color.colorGreen))
                    }
                    else -> {
                        tvItemStatusConfirmationPartnerRegister.text = "Ditolak"
                        tvItemStatusConfirmationPartnerRegister
                            .setTextColor(ContextCompat.getColorStateList(itemView.context, R.color.colorRed))
                    }
                }

            }
        }
    }

}