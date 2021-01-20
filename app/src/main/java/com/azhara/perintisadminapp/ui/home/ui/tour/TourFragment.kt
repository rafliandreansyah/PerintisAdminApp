package com.azhara.perintisadminapp.ui.home.ui.tour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.databinding.FragmentTourBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.google.android.material.snackbar.Snackbar

class TourFragment : Fragment() {

    private lateinit var tourAdapter: TourAdapter
    private val tourViewModel: TourViewModel by viewModels()
    private var _binding: FragmentTourBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tourViewModel.getDataTour()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTourBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tourAdapter = TourAdapter()

        setDataCar()
        isLoading()
        msgInfo()
    }

    private fun setDataCar(){
        tourViewModel.tourData.observe(viewLifecycleOwner, { dataCar ->
            tourAdapter.submitList(dataCar)
            with(binding){
                rvTour.layoutManager = LinearLayoutManager(context)
                rvTour.setHasFixedSize(true)
                rvTour.adapter = tourAdapter
            }
        })
    }

    private fun isLoading(){
        tourViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false  )
            }
        })
    }

    private fun msgInfo(){
        tourViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg != null){
                Snackbar.make(binding.containerTour, msg, Snackbar.LENGTH_LONG).setAction("Hide"){

                }.show()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}