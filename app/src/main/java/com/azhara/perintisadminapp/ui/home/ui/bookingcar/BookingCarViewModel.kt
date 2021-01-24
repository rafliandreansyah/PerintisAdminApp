package com.azhara.perintisadminapp.ui.home.ui.bookingcar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.BookingData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class BookingCarViewModel : ViewModel(){

    private val _bookingCarData = MutableLiveData<List<BookingData>>()
    val bookingCar = _bookingCarData

    private val _msg = MutableLiveData<String>()
    val msg = _msg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getDataBooking(){
        _isLoading.value = true
        val bookingDataDb = FirebaseConstants.firebaseDb.collection("booking_data")
        bookingDataDb.addSnapshotListener { value, error ->
            _isLoading.value = false
            if (error != null){
                _msg.value = error.message
            }
            if (value != null){
                _bookingCarData.postValue(value.toObjects(BookingData::class.java))
            }
        }
    }

}