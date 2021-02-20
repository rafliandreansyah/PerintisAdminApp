package com.azhara.perintisadminapp.ui.home.ui.car

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentDetailCarBinding
import com.azhara.perintisadminapp.entity.CarsData
import com.azhara.perintisadminapp.entity.MitraData
import com.azhara.perintisadminapp.entity.UserData
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.utils.Helper
import com.bumptech.glide.Glide

class DetailCarFragment : Fragment(), View.OnClickListener {

    private val carViewModel: CarViewModel by viewModels()
    private var carData: CarsData? = null
    private var phoneNumber: String? = null

    private var _binding: FragmentDetailCarBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carData = DetailCarFragmentArgs.fromBundle(arguments as Bundle).carData
        carViewModel.getDataPartner(carData?.partnerId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailCarBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnCallDetailCar?.setOnClickListener(this)

        getDataUser()
        msgInfo()
        isLoading()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnCallDetailCar -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")
                startActivity(intent)
            }

        }
    }

    private fun getDataUser(){
        carViewModel.mitraData.observe(viewLifecycleOwner, { userData ->
            setData(userData)
        })
    }

    private fun setData(mitraData: MitraData){

        with(binding){
            //Set data owner car
            phoneNumber = mitraData.phone.toString()
            this?.tvDetailCarOwnerEmail?.text = mitraData.email
            this?.tvDetailCarOwnerName?.text = mitraData.owner
            this?.tvDetailCarOwnerPhone?.text = mitraData.phone.toString()
            if (mitraData.imgUrl != null && mitraData.imgUrl != ""){
                context?.let { this?.imgDetailCarOwnerImage?.let { it1 -> Glide.with(it).load(mitraData.imgUrl).into(it1) } }
            }

            //Set data car
            if (carData?.imgUrl != null && carData?.imgUrl != ""){
                context?.let { this?.imgDetailCar?.let { it1 -> Glide.with(it).load(carData?.imgUrl).into(it1) } }
            }

            if (carData?.statusReady == true){
                this?.tvDetailCarStatusActive?.text = "Enable"
                this?.tvDetailCarStatusActive?.setTextColor(context?.let { ContextCompat.getColorStateList(it, R.color.colorGreen) })
            }
            else{
                this?.tvDetailCarStatusActive?.text = "Disable"
                this?.tvDetailCarStatusActive?.setTextColor(context?.let { ContextCompat.getColorStateList(it, R.color.colorRed) })

            }

            if (carData?.gear == 0){
                this?.tvDetailCarTransmission?.text = "Manual"
            }
            else{
                this?.tvDetailCarTransmission?.text = "Automatic"
            }

            this?.tvDetailCarName?.text = carData?.carName
            this?.tvDetailCarYear?.text = "Tahun ${carData?.year}"
            this?.tvDetailCarCapacity?.text = "${carData?.capacity} Orang"
            this?.tvDetailCarNoRegister?.text = carData?.carNumberPlate.toString()
            this?.tvDetailCarCapacityLuggage?.text = "${carData?.luggage.toString()} Koper"
            this?.tvDetailCarTotalPrice?.text = "Rp. ${carData?.price?.let { Helper.currencyFormat(it) }}"
        }

    }

    private fun isLoading(){
        carViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false  )
            }
        })
    }

    private fun msgInfo(){
        carViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg != null){
                binding?.containerDetailCar?.let { Helper.snackbar(msg, it) }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}