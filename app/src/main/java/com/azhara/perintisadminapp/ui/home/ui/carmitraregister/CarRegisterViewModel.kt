package com.azhara.perintisadminapp.ui.home.ui.carmitraregister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.CarMitraRegisterData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class CarRegisterViewModel : ViewModel(){

    private val _dataCarRegister = MutableLiveData<List<CarMitraRegisterData>>()
    val dataCarRegister = _dataCarRegister

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    fun getDataCarRegister(){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("car_register")
        db.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }
            if (value != null){
                _dataCarRegister.postValue(value.toObjects(CarMitraRegisterData::class.java))
            }
        }
    }

}