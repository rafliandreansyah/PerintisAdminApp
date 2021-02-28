package com.azhara.perintisadminapp.ui.home.ui.carmitraregister

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentDetailMitraCarRegisterBinding
import com.azhara.perintisadminapp.entity.CarMitraRegisterData
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.utils.Helper
import com.bumptech.glide.Glide


class DetailMitraCarRegisterFragment : Fragment(), View.OnClickListener {

    private var carMitraRegisterData: CarMitraRegisterData? = null
    private val carRegisterViewModel: CarRegisterViewModel by viewModels()

    private var _binding: FragmentDetailMitraCarRegisterBinding? = null
    private val binding get() = _binding!!
    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carMitraRegisterData = DetailMitraCarRegisterFragmentArgs.fromBundle(arguments as Bundle).carRegisterData
        carRegisterViewModel.loadDataMitra(carMitraRegisterData?.partnerId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailMitraCarRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmationDetailMitraCarRegister.setOnClickListener(this)
        binding.btnRejectDetailMitraCarRegister.setOnClickListener(this)
        binding.btnCallDetailMitraCarRegister.setOnClickListener(this)

        setData()
        isLoading()
        msg()
    }

    private fun setData(){
        carRegisterViewModel.dataMitra.observe(viewLifecycleOwner, { data ->
            if (data != null){

                with(binding){

                    /* <--- Set data mitra*/
                    context?.let { Glide.with(it).load(data.imgUrl).into(imgMitraImageDetailMitraCarRegister) }
                    tvMitraEmailDetailMitraCarRegister.text = data.email
                    tvMitraNameDetailMitraCarRegister.text = data.owner
                    tvPhoneDetailMitraCarRegister.text = "${data.phone}"
                    if (data.phone != null){
                        phoneNumber = "${data.phone}"
                    }
                    /*Set data mitra ---> */


                    /*Set data car*/
                    context?.let { Glide.with(it).load(carMitraRegisterData?.carImg).into(imgDetailCarRegister) }
                    tvCarNameDetailMitraCarRegister.text = carMitraRegisterData?.carType
                    tvTransmissionDetailMitraRegister.text = carMitraRegisterData?.carTransmission
                    tvCarYearDetailMitraCarRegister.text = "${carMitraRegisterData?.carYear}"
                    tvNumberRegisterDetailMitraCarRegister.text = carMitraRegisterData?.carNumber
                    when(carMitraRegisterData?.statusConfirm){
                        null -> {
                            tvStatusConfirmationDetailMitraCarRegister.text = "Menunggu Konfirmasi"
                            tvStatusConfirmationDetailMitraCarRegister.setTextColor(context?.let {
                                ContextCompat.getColorStateList(
                                    it, R.color.colorAccent)
                            })
                        }
                        1 -> {
                            tvStatusConfirmationDetailMitraCarRegister.text = "Diterima"
                            tvStatusConfirmationDetailMitraCarRegister.setTextColor(context?.let {
                                ContextCompat.getColorStateList(
                                    it, R.color.colorGreen)
                            })
                        }
                        else -> {
                            tvStatusConfirmationDetailMitraCarRegister.text = "Ditolak"
                            tvStatusConfirmationDetailMitraCarRegister.setTextColor(context?.let {
                                ContextCompat.getColorStateList(
                                    it, R.color.colorRed)
                            })
                        }
                    }
                    /*Set data car*/

                }

            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnCallDetailMitraCarRegister -> {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
                startActivity(intent)
            }
            R.id.btnConfirmationDetailMitraCarRegister -> {
                carMitraRegisterData?.let { carRegisterViewModel.statusConfirm(it, 1) }
            }
            R.id.btnRejectDetailMitraCarRegister -> {
                carMitraRegisterData?.let { carRegisterViewModel.statusConfirm(it, 0) }
            }
        }
    }

    private fun isLoading(){
        carRegisterViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        carRegisterViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg != null){
                when(msg){
                    "Success confirm" -> {
                        Helper.snackbar("Berhasil di konfirmasi", binding.containerDetailCarMitraRegister)
                        view?.findNavController()?.navigate(R.id.action_detailMitraCarRegisterFragment_to_nav_register_car)
                    }
                    "Reject" -> {
                        Helper.snackbar("Berhasil di tolak", binding.containerDetailCarMitraRegister)
                        view?.findNavController()?.navigate(R.id.action_detailMitraCarRegisterFragment_to_nav_register_car)
                    }
                    else -> binding.containerDetailCarMitraRegister.let { Helper.snackbar(msg, it) }
                }

            }
        })
    }

}