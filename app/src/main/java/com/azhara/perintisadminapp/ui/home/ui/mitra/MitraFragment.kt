package com.azhara.perintisadminapp.ui.home.ui.mitra

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentMitraBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.mitra.adapter.MitraAdapter
import com.google.android.material.snackbar.Snackbar


class MitraFragment : Fragment() {

    private lateinit var mitraAdapter: MitraAdapter
    private val mitraViewModel: MitraViewModel by viewModels()

    private var _binding: FragmentMitraBinding? = null
    val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mitraViewModel.getMitra()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMitraBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mitraAdapter = MitraAdapter()

        setDataMitra()
        isLoading()
        msg()
    }

    private fun setDataMitra(){
        mitraViewModel.dataMitra.observe(viewLifecycleOwner, { dataMitra ->
            mitraAdapter.submitList(dataMitra)
            with(binding){
                this?.rvMitra?.layoutManager = LinearLayoutManager(context)
                this?.rvMitra?.setHasFixedSize(true)
                this?.rvMitra?.adapter = mitraAdapter
            }
        })
    }

    private fun isLoading(){
        mitraViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        mitraViewModel.msg.observe(viewLifecycleOwner, { msg ->
            binding?.containerMitra?.let {
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