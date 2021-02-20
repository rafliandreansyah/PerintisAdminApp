package com.azhara.perintisadminapp.ui.home.ui.bookingtour.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.databinding.ItemDetailBookingTourBinding

class FacilityDetailBookingTourAdapter: ListAdapter<String, FacilityDetailBookingTourAdapter.FacilityDetailBookingTourViewHolder>(DIFF_UTIL) {

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FacilityDetailBookingTourAdapter.FacilityDetailBookingTourViewHolder {
        return FacilityDetailBookingTourViewHolder(ItemDetailBookingTourBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: FacilityDetailBookingTourAdapter.FacilityDetailBookingTourViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class FacilityDetailBookingTourViewHolder(private val binding: ItemDetailBookingTourBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(data: String){

            with(binding){

                tvItemDetailBookingTour.text = data

            }

        }

    }

}