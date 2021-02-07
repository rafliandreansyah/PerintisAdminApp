package com.azhara.perintisadminapp.ui.home.ui.car

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhara.perintisadminapp.databinding.FragmentCarBinding
import com.azhara.perintisadminapp.entity.CarsData
import com.azhara.perintisadminapp.ui.home.HomeActivity
import com.azhara.perintisadminapp.ui.home.ui.car.adapter.CarAdapter
import com.azhara.perintisadminapp.ui.home.ui.car.adapter.OnItemClickListener
import com.azhara.perintisadminapp.ui.home.ui.car.adapter.OnMenuClickListener
import com.azhara.perintisadminapp.utils.Helper
import com.google.android.material.snackbar.Snackbar

class CarFragment : Fragment() {

    private lateinit var carAdapter: CarAdapter
    private val carViewModel: CarViewModel by viewModels()
    private var _binding: FragmentCarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var backPressedTime: Long? = 0
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carViewModel.getDataCar()

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

        _binding = FragmentCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carAdapter = CarAdapter()

        setDataCar()
        isLoading()
        msgInfo()
        setOnItemClicked()
        setOnMenuClicked()
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

    private fun setOnItemClicked(){
        carAdapter.setOnItemClicked(object : OnItemClickListener{
            override fun onItemClicked(carData: CarsData) {
                val action = CarFragmentDirections.actionNavCarToDetailCarFragment(carData)
                view?.findNavController()?.navigate(action)
            }

        })
    }

    private fun setOnMenuClicked(){
        carAdapter.setOnMenuItemClicked(object : OnMenuClickListener{
            override fun onMenuClicked(carData: CarsData, typeAction: String?) {
                if (typeAction == "edit"){

                    if (carData.statusReady == true){
                        carViewModel.editStatusActive(carData.carNumberPlate, false)
                    }
                    else{
                        carViewModel.editStatusActive(carData.carNumberPlate, true)
                    }

                }
                else{
                    carViewModel.delete(carData.carNumberPlate)
                }
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
                Helper.snackbar(msg, binding.containerCar)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}