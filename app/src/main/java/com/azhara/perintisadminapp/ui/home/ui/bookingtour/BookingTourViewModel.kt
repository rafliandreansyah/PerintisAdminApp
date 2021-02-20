package com.azhara.perintisadminapp.ui.home.ui.bookingtour

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azhara.perintisadminapp.entity.BookingTourData
import com.azhara.perintisadminapp.entity.TourData
import com.azhara.perintisadminapp.entity.UserData
import com.azhara.perintisadminapp.entity.UserListBookingData
import com.azhara.perintisadminapp.utils.FirebaseConstants

class BookingTourViewModel : ViewModel(){
    private val _bookingTourData = MutableLiveData<List<BookingTourData>>()
    val bookingTourData = _bookingTourData

    private val _userData = MutableLiveData<UserData>()
    val userData = _userData

    private val _userListDataBooking = MutableLiveData<UserListBookingData>()
    val userListDataBooking = _userListDataBooking

    private val _dataTour = MutableLiveData<TourData>()
    val dataTour = _dataTour

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

    fun getDataUser(id: String?){

        _isLoading.value = true

        val userDb = id?.let { FirebaseConstants.firebaseDb.collection("users").document(it) }
        userDb?.get()?.addOnCompleteListener {

            _isLoading.value = false

            if (it.isSuccessful){
                _userData.postValue(it.result?.toObject(UserData::class.java))
            }
            else{
                _msg.value = it.exception?.message
            }

        }
    }

    fun getListBookingUser(id: String?, idList: String?){
        _isLoading.value = true

        val listBookingDb = id?.let { idList?.let { it1 ->
            FirebaseConstants.firebaseDb.collection("users").document(it).collection("listBooking").document(
                it1
            )
        } }
        listBookingDb?.get()?.addOnCompleteListener {
            _isLoading.value = false

            if (it.isSuccessful){
                _userListDataBooking.postValue(it.result?.toObject(UserListBookingData::class.java))
            }
            else{
                _msg.value = it.exception?.message
            }

        }

    }

    fun editDetailBookingTourUser(id: String?, idDetailBookingTour: String?, statusBooking: Int?){
        _isLoading.value = true
        val detailBookingUserDb =
            id?.let { idDetailBookingTour?.let { it1 ->
                FirebaseConstants.firebaseDb.collection("users").document(it).collection("bookingTour").document(
                    it1
                )
            } }

        detailBookingUserDb?.update("statusBooking", statusBooking)?.addOnCompleteListener {
            _isLoading.value = false
            if (it.isSuccessful){
                _msg.value = "Berhasil di konfirmasi"
            }
            else{
                _msg.value = it.exception?.message
            }

        }

    }

    fun getTour(id: String?){
        _isLoading.value = true
        val tourDb = id?.let { FirebaseConstants.firebaseDb.collection("tour").document(it) }
        tourDb?.get()?.addOnCompleteListener {
            _isLoading.value = false
            if (it.isSuccessful){
                _dataTour.postValue(it.result?.toObject(TourData::class.java))
            }
            else{
                _msg.value = it.exception?.message
            }

        }
    }
}