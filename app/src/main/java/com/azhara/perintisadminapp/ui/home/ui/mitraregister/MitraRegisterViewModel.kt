package com.azhara.perintisadminapp.ui.home.ui.mitraregister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.MitraRegisterData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class MitraRegisterViewModel : ViewModel(){

    private val _dataMitraRegister = MutableLiveData<List<MitraRegisterData>>()
    val dataMitraRegister = _dataMitraRegister

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getDataMitraRegister(){
        _isLoading.value = true
        val db = FirebaseConstants.firebaseDb.collection("partner_register")
        db.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }

            if (value != null){
                _dataMitraRegister.postValue(value.toObjects(MitraRegisterData::class.java))
            }
        }
    }

}