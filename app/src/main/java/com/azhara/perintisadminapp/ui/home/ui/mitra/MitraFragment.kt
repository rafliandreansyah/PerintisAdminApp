package com.azhara.perintisadminapp.ui.home.ui.mitra

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentMitraBinding
import com.azhara.perintisadminapp.entity.MitraData
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.mitra.adapter.MitraAdapter
import com.azhara.perintisadminapp.ui.home.ui.mitra.adapter.OnItemClickListener
import com.azhara.perintisadminapp.utils.Helper
import com.google.android.material.snackbar.Snackbar


class MitraFragment : Fragment(), View.OnClickListener {

    private lateinit var mitraAdapter: MitraAdapter
    private val mitraViewModel: MitraViewModel by viewModels()

    private var _binding: FragmentMitraBinding? = null
    val binding get() = _binding

    private var backPressedTime: Long? = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mitraViewModel.getMitra()

        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (backPressedTime!! + 2000 > System.currentTimeMillis()) {
                toast.cancel()
                activity?.moveTaskToBack(true)
                activity?.finish()
            }
            else {
                toast = Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT)
                toast.show()
            }
            backPressedTime = System.currentTimeMillis()
        }
        // The callback can be enabled or disabled here or in the lambda
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMitraBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.fabAddMitra?.setOnClickListener(this)

        mitraAdapter = MitraAdapter()

        setDataMitra()
        isLoading()
        msg()
        setOnItemMenuClicked()
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
                Helper.snackbar(msg, it)
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setOnItemMenuClicked(){
        mitraAdapter.setOnItemClicked(object : OnItemClickListener{

            override fun onItemClicked(mitraData: MitraData, statusMenu: String?) {

                if (statusMenu == "edit"){

                    if (mitraData.statusActive == true){
                        mitraViewModel.editStatusActive(mitraData.email, false)
                    }
                    else{
                        mitraViewModel.editStatusActive(mitraData.email, true)
                    }

                }
                else{

                    mitraViewModel.delete(mitraData.email)

                }

            }

        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fabAddMitra -> {
                view?.findNavController()?.navigate(R.id.action_nav_mitra_to_addMitraFragment)
            }
        }
    }

}