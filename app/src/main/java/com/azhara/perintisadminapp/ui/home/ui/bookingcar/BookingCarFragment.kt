package com.azhara.perintisadminapp.ui.home.ui.bookingcar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.R
import com.azhara.perintisadminapp.databinding.FragmentBookingCarBinding
import com.azhara.perintisadminapp.databinding.FragmentCarBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.bookingcar.adapter.BookingCarAdapter
import com.azhara.perintisadminapp.utils.Helper

class BookingCarFragment : Fragment() {

    private val bookingCarViewModel: BookingCarViewModel by viewModels()
    private lateinit var bookingCarAdapter: BookingCarAdapter

    private var _binding: FragmentBookingCarBinding? = null
    val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookingCarViewModel.getDataBooking()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookingCarBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingCarAdapter = BookingCarAdapter()

        setData()
        isLoading()
        msg()
    }

    private fun setData(){
        bookingCarViewModel.bookingCar.observe(viewLifecycleOwner, { dataBooking ->
            if (dataBooking != null){
                bookingCarAdapter.submitList(dataBooking)
                with(binding){
                    this?.rvBookingCar?.layoutManager = LinearLayoutManager(context)
                    this?.rvBookingCar?.setHasFixedSize(true)
                    this?.rvBookingCar?.adapter = bookingCarAdapter
                }
            }
        })
    }

    private fun isLoading(){
        bookingCarViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun msg(){
        bookingCarViewModel.msg.observe(viewLifecycleOwner, { msg ->
            if (msg != null){
                binding?.bookinCarContainer?.let { Helper.snackbar(msg, it) }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}