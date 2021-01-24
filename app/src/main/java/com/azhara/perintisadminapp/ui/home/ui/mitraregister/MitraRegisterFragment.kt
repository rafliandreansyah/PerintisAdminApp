package com.azhara.perintisadminapp.ui.home.ui.mitraregister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentMitraRegisterBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.mitraregister.adapter.MitraRegisterAdapter
import com.azhara.perintisadminapp.utils.Helper

class MitraRegisterFragment : Fragment() {

    private lateinit var mitraRegisterAdapter: MitraRegisterAdapter
    private val mitraRegisterViewModel: MitraRegisterViewModel by viewModels()
    private var _binding: FragmentMitraRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mitraRegisterViewModel.getDataMitraRegister()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMitraRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mitraRegisterAdapter = MitraRegisterAdapter()

        setData()
        isLoading()
        msg()
    }

    private fun setData(){
        mitraRegisterViewModel.dataMitraRegister.observe(viewLifecycleOwner, { dataBooking ->
            if (dataBooking != null){
                mitraRegisterAdapter.submitList(dataBooking)
                with(binding){
                    this?.rvMitraRegister?.layoutManager = LinearLayoutManager(context)
                    this?.rvMitraRegister?.setHasFixedSize(true)
                    this?.rvMitraRegister?.adapter = mitraRegisterAdapter
                }
            }
        })
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
                binding?.containerMitraRegister?.let { Helper.snackbar(msg, it) }
            }
        })
    }

}