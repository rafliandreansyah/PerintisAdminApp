package com.azhara.perintisadminapp.ui.home.ui.tour

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.TourData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class TourViewModel : ViewModel() {

    private val _tourData = MutableLiveData<List<TourData>>()
    val tourData = _tourData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    fun getDataTour(){
        _isLoading.value = true
        val dbTour = FirebaseConstants.firebaseDb.collection("tour")
        dbTour.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.postValue(error.message)
            }

            if (value != null){
                _tourData.postValue(value.toObjects(TourData::class.java))
            }
        }
    }
}