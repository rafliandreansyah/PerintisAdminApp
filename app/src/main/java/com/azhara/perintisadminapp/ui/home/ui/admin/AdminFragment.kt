package com.azhara.perintisadminapp.ui.home.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentAdminBinding
import com.azhara.perintisadminapp.databinding.FragmentMitraBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.admin.adapter.AdminAdapter
import com.azhara.perintisadminapp.ui.home.ui.mitra.MitraViewModel
import com.azhara.perintisadminapp.ui.home.ui.mitra.adapter.MitraAdapter
import com.google.android.material.snackbar.Snackbar


class AdminFragment : Fragment() {

    private lateinit var adminAdapter: AdminAdapter
    private val adminViewModel: AdminViewModel by viewModels()

    private var _binding: FragmentAdminBinding? = null
    val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adminViewModel.getAdmin()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adminAdapter = AdminAdapter()

        setDataAdmin()
        isLoading()
        msg()
    }

    private fun setDataAdmin(){
        adminViewModel.dataAdmin.observe(viewLifecycleOwner, { dataAdmin ->
            adminAdapter.submitList(dataAdmin)
            with(binding){
                this?.rvAdmin?.layoutManager = LinearLayoutManager(context)
                this?.rvAdmin?.setHasFixedSize(true)
                this?.rvAdmin?.adapter = adminAdapter
            }
        })
    }

    private fun isLoading(){
        adminViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        adminViewModel.msg.observe(viewLifecycleOwner, { msg ->
            binding?.containerAdmin?.let {
                Snackbar.make(it, msg, Snackbar.LENGTH_LONG).setAction("Hide"){

                }.show()
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}