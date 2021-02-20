package com.azhara.perintisadminapp.ui.home.ui.tour.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.ItemTourBinding
import com.azhara.perintisadminapp.entity.CarsData
import com.azhara.perintisadminapp.entity.TourData
import com.azhara.perintisadminapp.ui.home.ui.car.adapter.OnMenuClickListener
import com.azhara.perintisadminapp.utils.Helper

class TourAdapter : ListAdapter<TourData, TourAdapter.TourViewHolder>(DIFF_UTIL) {

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<TourData>(){
            override fun areItemsTheSame(oldItem: TourData, newItem: TourData): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: TourData, newItem: TourData): Boolean =
                    oldItem.statusReady == newItem.statusReady

        }
    }

    private var onMenuClickListener: OnMenuClickListener? = null

    fun setOnMenuItemClicked(onMenuClickListener: OnMenuClickListener?){
        this.onMenuClickListener = onMenuClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourViewHolder {
        return TourViewHolder(ItemTourBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TourViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TourViewHolder(val binding: ItemTourBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(tourData: TourData){
            with(binding){
                tvItemTourName.text = tourData.tourName
                tvItemTourCapacity.text = "${tourData.capacity} Orang"
                tvItemTourDuration.text = tourData.durationTour
                tvItemTourPrice.text = "Rp. ${tourData.price?.let { Helper.currencyFormat(it) }}/Orang"
                if (tourData.statusReady == true){
                    tvItemTourStatusEnabled.text = "Enable"
                    tvItemTourStatusEnabled.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreen))
                }else{
                    tvItemTourStatusEnabled.text = "Disable"
                    tvItemTourStatusEnabled.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorRed))
                }

                btnOptionTour.setOnClickListener {
                    val popUpMenu = PopupMenu(itemView.context, btnOptionTour)
                    popUpMenu.inflate(R.menu.menu_list_tour)
                    if (tourData.statusReady == true){
                        popUpMenu.menu[0].title = "Non-aktifkan"
                    }
                    else{
                        popUpMenu.menu[0].title = "Aktifkan"
                    }
                    popUpMenu.setOnMenuItemClickListener {
                        when(it.itemId){
                            R.id.optionEditStatusActive -> {
                                onMenuClickListener?.onMenuClicked(tourData, "change status")
                            }
                            R.id.optionDelete -> {
                                onMenuClickListener?.onMenuClicked(tourData, "delete")
                            }
                        }
                        false
                    }

                    popUpMenu.show()

                }
            }
        }
    }

    interface OnMenuClickListener{
        fun onMenuClicked(tourData: TourData, typeAction: String?)
    }
}