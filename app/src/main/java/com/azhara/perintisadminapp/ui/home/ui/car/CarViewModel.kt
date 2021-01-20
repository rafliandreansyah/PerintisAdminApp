package com.azhara.perintisadminapp.ui.home.ui.car

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.CarsData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class CarViewModel : ViewModel() {


    private val _carData = MutableLiveData<List<CarsData>>()
    val carData = _carData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    fun getDataCar(){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("cars")
        db.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.postValue(error.message)
            }
            if (value != null){
                _carData.postValue(value.toObjects(CarsData::class.java))
            }
        }
    }
}