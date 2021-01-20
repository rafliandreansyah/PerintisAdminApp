package com.azhara.perintisadminapp.ui.home.ui.car

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.databinding.FragmentCarBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.google.android.material.snackbar.Snackbar

class CarFragment : Fragment() {

    private lateinit var carAdapter: CarAdapter
    private val carViewModel: CarViewModel by viewModels()
    private var _binding: FragmentCarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carViewModel.getDataCar()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carAdapter = CarAdapter()

        setDataCar()
        isLoading()
        msgInfo()
    }

    private fun setDataCar(){
        carViewModel.carData.observe(viewLifecycleOwner, { dataCar ->
            carAdapter.submitList(dataCar)
            with(binding){
                rvCar.layoutManager = LinearLayoutManager(context)
                rvCar.setHasFixedSize(true)
                rvCar.adapter = carAdapter
            }
        })
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
                Snackbar.make(binding.containerCar, msg, Snackbar.LENGTH_LONG).setAction("Hide"){

                }.show()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}