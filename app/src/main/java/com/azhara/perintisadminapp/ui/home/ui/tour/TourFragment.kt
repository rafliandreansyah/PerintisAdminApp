package com.azhara.perintisadminapp.ui.home.ui.tour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.databinding.FragmentTourBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.utils.Helper
import com.google.android.material.snackbar.Snackbar

class TourFragment : Fragment() {

    private lateinit var tourAdapter: TourAdapter
    private val tourViewModel: TourViewModel by viewModels()
    private var _binding: FragmentTourBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var backPressedTime: Long? = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tourViewModel.getDataTour()

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
                Helper.snackbar(msg, binding.containerTour)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}