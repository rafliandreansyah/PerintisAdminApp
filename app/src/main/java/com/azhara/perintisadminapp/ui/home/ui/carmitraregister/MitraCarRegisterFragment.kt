package com.azhara.perintisadminapp.ui.home.ui.carmitraregister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.databinding.FragmentMitraCarRegisterBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.carmitraregister.adapter.CarMitraRegisterAdapter
import com.azhara.perintisadminapp.utils.Helper
import com.google.android.material.snackbar.Snackbar

class MitraCarRegisterFragment : Fragment() {

    private lateinit var carMitraRegisterAdapter: CarMitraRegisterAdapter
    private val carRegisterViewModel: CarRegisterViewModel by viewModels()

    private var _binding: FragmentMitraCarRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carRegisterViewModel.getDataCarRegister()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMitraCarRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carMitraRegisterAdapter = CarMitraRegisterAdapter()

        setDataCarMitraRegister()
        isLoading()
        msgInfo()
    }

    private fun setDataCarMitraRegister(){
        carRegisterViewModel.dataCarRegister.observe(viewLifecycleOwner, { dataCarRegister ->
            carMitraRegisterAdapter.submitList(dataCarRegister)
            with(binding){
                this?.rvCarMitraRegister?.layoutManager = LinearLayoutManager(context)
                this?.rvCarMitraRegister?.setHasFixedSize(true)
                this?.rvCarMitraRegister?.adapter = carMitraRegisterAdapter
            }
        })
    }

    private fun isLoading(){
        carRegisterViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false  )
            }
        })
    }

    private fun msgInfo(){
        carRegisterViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg != null){
                binding?.containerCarMitraRegister?.let { Helper.snackbar(msg, it) }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}