package com.azhara.perintisadminapp.ui.home.ui.bookingtour.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.ItemBookingTourBinding
import com.azhara.perintisadminapp.entity.BookingTourData
import com.azhara.perintisadminapp.utils.Helper

class BookingTourAdapter: ListAdapter<BookingTourData, BookingTourAdapter.BookingTourViewHolder>(DIFF_UTIL) {

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<BookingTourData>(){
            override fun areItemsTheSame(oldItem: BookingTourData, newItem: BookingTourData): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: BookingTourData, newItem: BookingTourData): Boolean =
                    oldItem.tourId == newItem.tourId

        }
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClick(onItemClickListener: OnItemClickListener?){
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingTourAdapter.BookingTourViewHolder =
            BookingTourViewHolder(ItemBookingTourBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: BookingTourAdapter.BookingTourViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookingTourViewHolder(val binding: ItemBookingTourBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: BookingTourData){
            with(binding){
                tvItemBookingTourDuration.text = data.duration
                tvItemBookingTourNameTour.text = data.tourName
                if (data.statusPayment == false){
                    tvItemBookingTourStatus.text = "Menunggu Konfirmasi"
                    tvItemBookingTourStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
                }else if (data.statusPayment == true){
                    tvItemBookingTourStatus.text = "Terkonfirmasi"
                    tvItemBookingTourStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreen))
                }
                tvItemBookingTourTotalPrice.text = "Rp. ${data.totalPrice?.let { Helper.currencyFormat(it) }}"
                tvItemBookingTourUserName.text = data.userBookingName

                containerItemBookingTour.setOnClickListener {
                    onItemClickListener?.onItemClicked(data)
                }
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClicked(data: BookingTourData)
    }
}