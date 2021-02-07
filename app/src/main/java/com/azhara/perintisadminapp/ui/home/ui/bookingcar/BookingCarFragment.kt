package com.azhara.perintisadminapp.ui.home.ui.bookingcar

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
import com.azhara.perintisadminapp.databinding.FragmentBookingCarBinding
import com.azhara.perintisadminapp.entity.BookingData
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.bookingcar.adapter.BookingCarAdapter
import com.azhara.perintisadminapp.utils.Helper

class BookingCarFragment : Fragment() {

    private val bookingCarViewModel: BookingCarViewModel by viewModels()
    private lateinit var bookingCarAdapter: BookingCarAdapter

    private var _binding: FragmentBookingCarBinding? = null
    val binding get() = _binding

    private var backPressedTime: Long? = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookingCarViewModel.getDataBooking()

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
        _binding = FragmentBookingCarBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingCarAdapter = BookingCarAdapter()

        setData()
        isLoading()
        msg()
        onItemClicked()
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

    private fun onItemClicked(){
        bookingCarAdapter.setOnItemClicked(object : BookingCarAdapter.OnItemClickBookingCarListener{
            override fun onItemClicked(bookingData: BookingData) {
                val action = BookingCarFragmentDirections.actionNavBookingCarToDetailBookingCarFragment(bookingData)
                view?.findNavController()?.navigate(action)
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