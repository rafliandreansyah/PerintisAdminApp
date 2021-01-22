package com.azhara.perintisadminapp.ui.home.ui.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.AdminData
import com.azhara.perintisadminapp.entity.MitraData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class AdminViewModel: ViewModel() {

    private val _dataAdmin = MutableLiveData<List<AdminData>>()
    val dataAdmin = _dataAdmin

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getAdmin(){
        _isLoading.value = true
        val adminDb = FirebaseConstants.firebaseDb.collection("admin")
        adminDb.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }
            if (value != null){
                _dataAdmin.postValue(value.toObjects(AdminData::class.java))
            }
        }
    }

}