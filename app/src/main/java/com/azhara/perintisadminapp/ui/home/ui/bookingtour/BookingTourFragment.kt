package com.azhara.perintisadminapp.ui.home.ui.bookingtour

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentBookingTourBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.bookingtour.adapter.BookingTourAdapter
import com.azhara.perintisadminapp.utils.Helper
import com.google.android.material.snackbar.Snackbar

class BookingTourFragment : Fragment() {

    private val bookingTourViewModel: BookingTourViewModel by viewModels()
    private lateinit var bookingTourAdapter: BookingTourAdapter

    private var _binding: FragmentBookingTourBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookingTourViewModel.getDataBookingTour()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookingTourBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingTourAdapter = BookingTourAdapter()
        setDataBookingTour()
        isLoading()
        msgInfo()
    }

    private fun setDataBookingTour(){
        bookingTourViewModel.bookingTourData.observe(viewLifecycleOwner, { dataCar ->
            bookingTourAdapter.submitList(dataCar)
            with(binding){
                this?.rvBookingTour?.layoutManager = LinearLayoutManager(context)
                this?.rvBookingTour?.setHasFixedSize(true)
                this?.rvBookingTour?.adapter = bookingTourAdapter
            }
        })
    }

    private fun isLoading(){
        bookingTourViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false  )
            }
        })
    }

    private fun msgInfo(){
        bookingTourViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg != null){
                binding?.containerBookingTour?.let { Helper.snackbar(msg, it) }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}