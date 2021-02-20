package com.azhara.perintisadminapp.ui.home.ui.tour.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.databinding.ItemFacilityBinding

class FacilityAdapter: ListAdapter<String, FacilityAdapter.FacilityViewHolder>(DIFF_UTIL) {

    private var onItemCancelListener: OnItemCancelListener? = null

    fun setOnItemCancelFacility(onItemCancelListener: OnItemCancelListener?){
        this.onItemCancelListener = onItemCancelListener
    }

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
    ): FacilityAdapter.FacilityViewHolder =
        FacilityViewHolder(ItemFacilityBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FacilityAdapter.FacilityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FacilityViewHolder(val binding: ItemFacilityBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(dataFacility: String){

            with(binding){
                tvItemFacility.text = dataFacility

                btnItemCancelFacilityTour.setOnClickListener {
                    onItemCancelListener?.onItemCancel(dataFacility)
                }
            }

        }

    }

    interface OnItemCancelListener{
        fun onItemCancel(dataFacility: String)
    }

}