package com.azhara.perintisadminapp.ui.home.ui.mitraregister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentDetailMitraRegisterBinding
import com.azhara.perintisadminapp.entity.MitraRegisterData
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.utils.Helper
import com.bumptech.glide.Glide

class DetailMitraRegisterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentDetailMitraRegisterBinding? = null
    private val binding get() = _binding!!

    private val mitraRegisterViewModel: MitraRegisterViewModel by viewModels()

    private var dataMitraRegister: MitraRegisterData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataMitraRegister = DetailMitraRegisterFragmentArgs.fromBundle(arguments as Bundle).mitraRegisterData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailMitraRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            btnConfirmationDetailMitraRegister.setOnClickListener(this@DetailMitraRegisterFragment)
            btnRejectDetailMitraRegister.setOnClickListener(this@DetailMitraRegisterFragment)
        }

        setData()
        msg()
        isLoading()
    }

    private fun setData(){

        with(binding){

            if (dataMitraRegister != null){

                context?.let { Glide.with(it).load(dataMitraRegister?.carImg).into(imgDetailCarMitraRegister) }
                tvCarNameDetailMitraRegister.text = dataMitraRegister?.carType
                tvCarYearDetailMitraRegister.text = "${dataMitraRegister?.carYear}"
                tvNumberRegisterDetailMitraRegister.text = dataMitraRegister?.carNumberRegister

                if (dataMitraRegister?.carTransmision == 0){
                    tvTransmissionDetailMitraRegister.text = "Manual"
                }
                else{
                    tvTransmissionDetailMitraRegister.text = "Automatic"
                }

                when (dataMitraRegister?.statusConfirm) {
                    null -> {
                        tvStatusConfirmationDetailMitraRegister.text = "Menunggu Konfirmasi"
                        tvStatusConfirmationDetailMitraRegister.setTextColor(context?.let {
                            ContextCompat.getColorStateList(
                                it, R.color.colorAccent)
                        })
                    }
                    1 -> {
                        tvStatusConfirmationDetailMitraRegister.text = "Diterima"
                        tvStatusConfirmationDetailMitraRegister.setTextColor(context?.let {
                            ContextCompat.getColorStateList(
                                it, R.color.colorGreen)
                        })
                        btnRejectDetailMitraRegister.visibility = View.INVISIBLE
                        btnConfirmationDetailMitraRegister.visibility = View.INVISIBLE
                    }
                    else -> {
                        tvStatusConfirmationDetailMitraRegister.text = "Ditolak"
                        tvStatusConfirmationDetailMitraRegister.setTextColor(context?.let {
                            ContextCompat.getColorStateList(
                                it, R.color.colorRed)
                        })
                        btnRejectDetailMitraRegister.visibility = View.INVISIBLE
                        btnConfirmationDetailMitraRegister.visibility = View.INVISIBLE
                    }
                }

                tvInfoNameDetailMitraRegister.text = dataMitraRegister?.name
                tvInfoAddressDetailMitraRegister.text = dataMitraRegister?.address
                tvInfoEmailDetailMitraRegister.text = dataMitraRegister?.email
                tvInfoPhoneDetailMitraRegister.text = "${dataMitraRegister?.phoneNumber}"

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnConfirmationDetailMitraRegister -> {
                mitraRegisterViewModel.statusConfirmMitraRegister(dataMitraRegister, "accept")
            }
            R.id.btnRejectDetailMitraRegister -> {
                mitraRegisterViewModel.statusConfirmMitraRegister(dataMitraRegister, "reject")
            }
        }
    }

    private fun isLoading(){
        mitraRegisterViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        mitraRegisterViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg != null){
                when(msg){
                    "Success confirm" -> {
                        view?.findNavController()?.navigate(R.id.action_detailMitraRegisterFragment_to_nav_mitra_register)
                    }
                    "Reject" -> {
                        view?.findNavController()?.navigate(R.id.action_detailMitraRegisterFragment_to_nav_mitra_register)
                    }
                    else -> binding.containerDetailMitraRegister.let { Helper.snackbar(msg, it) }
                }

            }
        })
    }

}