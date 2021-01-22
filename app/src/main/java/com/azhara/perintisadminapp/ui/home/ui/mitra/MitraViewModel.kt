package com.azhara.perintisadminapp.ui.home.ui.mitra

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.MitraData
import com.azhara.perintisadminapp.entity.UserData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class MitraViewModel: ViewModel() {

    private val _dataMitra = MutableLiveData<List<MitraData>>()
    val dataMitra = _dataMitra

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getMitra(){
        _isLoading.value = true
        val mitraDb = FirebaseConstants.firebaseDb.collection("partners")
        mitraDb.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }
            if (value != null){
                _dataMitra.postValue(value.toObjects(MitraData::class.java))
            }
        }
    }

}