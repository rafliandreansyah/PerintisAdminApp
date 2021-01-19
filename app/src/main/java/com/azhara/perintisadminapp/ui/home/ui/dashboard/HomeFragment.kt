package com.azhara.perintisadminapp.ui.home.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.databinding.FragmentHomeBinding
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.dashboard.adapter.BookingDashboardAdapter
import com.azhara.perintisadminapp.ui.home.ui.dashboard.adapter.MitraRegisterAdapter
import com.azhara.perintisadminapp.utils.Helper
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null

    private lateinit var bookingDashboardAdapter: BookingDashboardAdapter
    private lateinit var mitraRegisterAdapter: MitraRegisterAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel.getCar()
        homeViewModel.getMitra()
        homeViewModel.getTour()
        homeViewModel.getUser()
        homeViewModel.getBookingCar()
        homeViewModel.getPengajuanMitraConfirmation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingDashboardAdapter = BookingDashboardAdapter()
        mitraRegisterAdapter = MitraRegisterAdapter()

        dataCar()
        dataMitra()
        dataTour()
        dataUser()
        dataBookingCar()
        dataMitraRegister()
        isLoading()
        errorMessage()
    }

    private fun dataCar(){
        homeViewModel.dataCar.observe(viewLifecycleOwner, {dataCar ->
            if (dataCar != null){
                binding.tvDashboardCarCount.text = dataCar.count().toString()
            }
        })
    }

    private fun dataMitra(){
        homeViewModel.dataMitra.observe(viewLifecycleOwner, { dataMitra ->
            if (dataMitra != null){
                binding.tvDashboardMitraCount.text = dataMitra.count().toString()
            }
        })
    }

    private fun dataTour(){
        homeViewModel.dataTour.observe(viewLifecycleOwner, { dataTour ->
            if (dataTour != null){
                binding.tvDashboardTourCount.text = dataTour.count().toString()
            }
        })
    }

    private fun dataUser(){
        homeViewModel.dataUsers.observe(viewLifecycleOwner, { dataUser ->
            if (dataUser != null){
                binding.tvDashboardUserCount.text = dataUser.count().toString()
            }
        })
    }

    private fun dataBookingCar(){
        homeViewModel.dataBookingCar.observe(viewLifecycleOwner, { dataBookingCar ->
            if (dataBookingCar != null) {
                var totalIncome = 0L
                val dataCarWaitingConfirm = dataBookingCar.filter {
                    it.statusBooking == null
                }
                val dataCarOnProgress = dataBookingCar.filter {
                    it.statusBooking == 0
                }
                val dataCarDone = dataBookingCar.filter {
                    it.statusBooking == 1
                }
                dataCarDone.forEach {
                    totalIncome += it.totalPrice!!
                }
                bookingDashboardAdapter.submitList(dataCarWaitingConfirm)
                binding.rvBookingCar.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.rvBookingCar.setHasFixedSize(true)
                binding.rvBookingCar.adapter = bookingDashboardAdapter

                binding.tvDashboardCarBookingWaitingConfirm.text = dataCarWaitingConfirm.count().toString()
                binding.tvDashboardCarBookingOnProgress.text = dataCarOnProgress.count().toString()
                binding.tvDashboardCarBookingDone.text = dataCarDone.count().toString()
                binding.tvDashboardTotalIncome.text = "Rp. ${Helper.currencyFormat(totalIncome)}"

            } else {
                binding.tvTitleBookingCar.visibility = View.INVISIBLE
                binding.tvSeeAllBookingMobil.visibility = View.INVISIBLE
            }
        })
    }

    private fun dataMitraRegister(){
        homeViewModel.dataMitraRegister.observe(viewLifecycleOwner, { dataMitraRegister ->
            if (dataMitraRegister != null){
                mitraRegisterAdapter.submitList(dataMitraRegister)
                binding.rvPengajuanMitra.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.rvPengajuanMitra.setHasFixedSize(true)
                binding.rvPengajuanMitra.adapter = mitraRegisterAdapter
            }else{
                binding.tvTitleMitraRegister.visibility = View.INVISIBLE
                binding.tvSeeAllPengajuanMitra.visibility = View.INVISIBLE
            }
        })
    }

    private fun isLoading(){
        homeViewModel.isLoading.observe(viewLifecycleOwner, {isLoading ->
            if (isLoading == true){
                (activity as HomeActivity).isLoading(true)
            }else{
                (activity as HomeActivity).isLoading(false)
            }
        })
    }

    private fun errorMessage(){
        homeViewModel.errorMsg.observe(viewLifecycleOwner, { errorMessage ->
            if (errorMessage != null){
                Snackbar.make(binding.containerFragmenHome, errorMessage, Snackbar.LENGTH_LONG)
                        .setAction("Reload"){
                            dataCar()
                            dataMitra()
                            dataTour()
                            dataUser()
                            dataBookingCar()
                            dataMitraRegister()
                        }
                        .show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}