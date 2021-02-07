package com.azhara.perintisadminapp.ui.home.ui.mitra.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.ItemMitraBinding
import com.azhara.perintisadminapp.entity.MitraData
import com.bumptech.glide.Glide

class MitraAdapter : ListAdapter<MitraData, MitraAdapter.MitraViewHolder>(DIFF_UTIL){

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<MitraData>(){
            override fun areItemsTheSame(oldItem: MitraData, newItem: MitraData): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: MitraData, newItem: MitraData): Boolean =
                    oldItem.email == newItem.email

        }
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClicked(onItemClickListener: OnItemClickListener?){
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MitraAdapter.MitraViewHolder =
            MitraViewHolder(ItemMitraBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MitraAdapter.MitraViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MitraViewHolder(val binding: ItemMitraBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(mitraData: MitraData){
            with(binding){
                tvItemMitraEmail.text = mitraData.email
                tvItemMitraName.text = mitraData.owner

                if (mitraData.statusActive == false){
                    tvItemMitraEmail.setTextColor(ContextCompat.getColor(itemView.context, R.color.common_google_signin_btn_text_dark_disabled))
                    tvItemMitraName.setTextColor(ContextCompat.getColor(itemView.context, R.color.common_google_signin_btn_text_dark_disabled))
                }

                if (mitraData.imgUrl != null && mitraData.imgUrl != ""){
                    Glide.with(itemView).load(mitraData.imgUrl).into(imgItemMitra)
                }

                btnOptionMitra.setOnClickListener {
                    val popUpMenu = PopupMenu(itemView.context, btnOptionMitra)
                    popUpMenu.inflate(R.menu.menu_list_mitra)
                    if (mitraData.statusActive == true){
                        popUpMenu.menu.getItem(0).title = "Non Aktif"
                    }else{
                        popUpMenu.menu.getItem(0).title = "Aktifkan"
                    }
                    popUpMenu.setOnMenuItemClickListener {
                        when(it.itemId){
                            R.id.editMitraStatusActive -> {
                                onItemClickListener?.onItemClicked(mitraData, "edit")
                            }
                            R.id.deleteMitra -> {
                                onItemClickListener?.onItemClicked(mitraData, "delete")
                            }
                        }
                        true
                    }
                    popUpMenu.show()
                }

            }
        }
    }

}

interface OnItemClickListener{
    fun onItemClicked(mitraData: MitraData, statusMenu: String?)
}