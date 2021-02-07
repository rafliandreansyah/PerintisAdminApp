package com.azhara.perintisadminapp.ui.home.ui.carmitraregister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
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

    private var backPressedTime: Long? = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carRegisterViewModel.getDataCarRegister()

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