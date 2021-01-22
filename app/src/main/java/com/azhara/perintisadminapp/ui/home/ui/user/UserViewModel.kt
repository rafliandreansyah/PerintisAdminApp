package com.azhara.perintisadminapp.ui.home.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.UserData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class UserViewModel: ViewModel() {

    private val _dataUser = MutableLiveData<List<UserData>>()
    val dataUser = _dataUser

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getUser(){
        _isLoading.value = true
        val userDb = FirebaseConstants.firebaseDb.collection("users")
        userDb.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }
            if (value != null){
                _dataUser.postValue(value.toObjects(UserData::class.java))
            }
        }
    }

}