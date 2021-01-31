package com.azhara.perintisadminapp.ui.home.ui.bookingcar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.ItemBookingCarBinding
import com.azhara.perintisadminapp.entity.BookingData
import com.azhara.perintisadminapp.utils.Helper

class BookingCarAdapter: ListAdapter<BookingData, BookingCarAdapter.BookingCarViewHolder>(DIFF_UTIL) {

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<BookingData>(){
            override fun areItemsTheSame(oldItem: BookingData, newItem: BookingData): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: BookingData, newItem: BookingData): Boolean =
                    oldItem.statusBooking == newItem.statusBooking

        }
    }

    private var onItemClickedBookingCarListener: OnItemClickBookingCarListener? = null

    fun setOnItemClicked(onItemClickedBookingCarListener: OnItemClickBookingCarListener){
        this.onItemClickedBookingCarListener = onItemClickedBookingCarListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingCarAdapter.BookingCarViewHolder =
            BookingCarViewHolder(ItemBookingCarBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: BookingCarAdapter.BookingCarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookingCarViewHolder(val binding: ItemBookingCarBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bookingData: BookingData){
            with(binding){
                tvItemCarBookingUserName.text = bookingData.userName
                tvItemBookingCarDate.text = "${bookingData.startDate?.seconds?.let { Helper.convertToLocalDate(it) }}"
                tvItemBookingCarName.text = bookingData.carName
                tvItemCarBookingDuration.text = "${bookingData.duration} Hari"
                tvItemCarBookingTotalPrice.text = "Rp. ${bookingData.totalPrice?.let { Helper.currencyFormat(it) }}"
                when (bookingData.statusBooking) {
                    "null" -> {
                        tvItemKonfirmationStatus.text = "Menunggu Konfirmasi"
                        tvItemKonfirmationStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorRed))
                    }
                    "0" -> {
                        tvItemKonfirmationStatus.text = "On Progress"
                        tvItemKonfirmationStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
                    }
                    "1" -> {
                        tvItemKonfirmationStatus.text = "Selesai"
                        tvItemKonfirmationStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreen))
                    }
                }
                itemBookingCar.setOnClickListener {
                    onItemClickedBookingCarListener?.onItemClicked(bookingData)
                }
            }
        }
    }

    interface OnItemClickBookingCarListener{
        fun onItemClicked(bookingData: BookingData)
    }

}