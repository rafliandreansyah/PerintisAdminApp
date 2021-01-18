package com.azhara.perintisadminapp.ui.home.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.ItemBookedCarDashboardBinding
import com.azhara.perintisadminapp.entity.BookingData
import com.azhara.perintisadminapp.utils.Helper
import com.bumptech.glide.Glide

class BookingDashboardAdapter: ListAdapter<BookingData, BookingDashboardAdapter.BookingDashboardViewHolder>(DIFF_UTIL) {

    companion object{
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<BookingData>(){
            override fun areItemsTheSame(oldItem: BookingData, newItem: BookingData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: BookingData, newItem: BookingData): Boolean {
                return oldItem.bookedListIdUser == newItem.bookedListIdUser
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingDashboardAdapter.BookingDashboardViewHolder {
        val binding = ItemBookedCarDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingDashboardViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BookingDashboardAdapter.BookingDashboardViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class BookingDashboardViewHolder(private val binding: ItemBookedCarDashboardBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(bookingData: BookingData){
            with(binding){
                val date = bookingData.startDate?.seconds?.let { Helper.convertToLocalDate(it) }
                if (bookingData.userImgUrl != null && bookingData.userImgUrl != ""){
                    Glide.with(itemView.context).load(bookingData.userImgUrl).into(itemImgUserBooking)
                }
                itemUserBookingName.text = bookingData.userName
                if (bookingData.statusBooking == null){
                    itemStatusBooking.text = "Menunggu Konfirmasi"
                    itemStatusBooking.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorRed))
                }else if(bookingData.statusBooking == 0){
                    itemStatusBooking.text = "On Progress"
                    itemStatusBooking.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
                }else if (bookingData.statusBooking == 1){
                    itemStatusBooking.text = "Selesai"
                    itemStatusBooking.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreen))
                }
                itemCarNameBooking.text = bookingData.carName
                itemDateBooking.text = date
            }
        }
    }
}