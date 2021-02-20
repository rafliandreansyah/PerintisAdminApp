package com.azhara.perintisadminapp.ui.home.ui.tour.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.databinding.ItemTourVisitedBinding

class TourVisitedAdapter: ListAdapter<String, TourVisitedAdapter.TourVisitedViewHolder>(DIFF_UTIL) {

    private var onItemCancelListener: OnItemCancelListener? = null

    fun setOnItemCancelVisitedTour(onItemCancelListener: OnItemCancelListener?){
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
    ): TourVisitedAdapter.TourVisitedViewHolder =
        TourVisitedViewHolder(ItemTourVisitedBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TourVisitedAdapter.TourVisitedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TourVisitedViewHolder(val binding: ItemTourVisitedBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(dataVisitedTour: String){

            with(binding){
                tvItemVisitedTour.text = dataVisitedTour

                btnCancelVisitedTour.setOnClickListener {
                    onItemCancelListener?.onItemCancel(dataVisitedTour)
                }
            }

        }

    }

    interface OnItemCancelListener{
        fun onItemCancel(dataVisitedTour: String)
    }

}