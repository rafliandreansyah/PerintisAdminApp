package com.azhara.perintisadminapp.ui.home.ui.bookingtour

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
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

    private var backPressedTime: Long? = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookingTourViewModel.getDataBookingTour()

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