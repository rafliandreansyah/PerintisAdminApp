package com.azhara.perintisadminapp.ui.home.ui.bookingtour

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.BookingTourData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class BookingTourViewModel : ViewModel(){
    private val _bookingTourData = MutableLiveData<List<BookingTourData>>()
    val bookingTourData = _bookingTourData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    fun getDataBookingTour(){
        _isLoading.value = true
        val dbTour = FirebaseConstants.firebaseDb.collection("booking_tour")
        dbTour.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.postValue(error.message)
            }

            if (value != null){
                _bookingTourData.postValue(value.toObjects(BookingTourData::class.java))
            }
        }
    }
}